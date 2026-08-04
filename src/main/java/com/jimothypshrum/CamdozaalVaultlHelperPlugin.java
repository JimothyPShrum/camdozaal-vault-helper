package com.jimothypshrum;

import com.google.common.collect.Lists;
import com.google.inject.Provides;
import com.google.common.base.Splitter;
import javax.inject.Inject;
import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;


import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.api.gameval.ItemID;


@Slf4j
@PluginDescriptor(
        name = "Camdozaal Vault Helper"
)


public class CamdozaalVaultlHelperPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private CamdozaalVaultHelperConfig config;

    @Inject
    private CamdozaalVaultHelperOverlay camdozaalVaultHelperOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Getter
    private final static HashMap<String, GameObject> vaultObjects = new HashMap<>();
    private final static HashMap<String, String> objectNumbers = new HashMap<>();
    private StringBuilder objectStates = new StringBuilder(30);
    private String barriers = "";
    private String pedestals = "";
    private static boolean prioritizeElaborate = false;
    private boolean needToReset = false;
    @Getter
    private String[] fullPath = {};
    @Getter
    private String[] pathUniqueNodes = {};
    private int pathNextIndex = 1;
    @Getter
    private String pathNextNode = "EXIT";
    private final HashMap<Integer, int[]> pathNextActivationCoords = new HashMap<>();

    private BarroniteInfoBox infoBox;
    private int barroniteCount = -1;

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(camdozaalVaultHelperOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(camdozaalVaultHelperOverlay);
        infoBoxManager.removeInfoBox(infoBox);
        infoBox = null;
        barroniteCount = -1;
        resetAll();
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted event){
        if(event.getCommand().equals("test")){
            String states = "100101000110001011101011011111";
            convertObjStates(states);
            log.debug(barriers+" "+pedestals);

            String[] path = getBestPathEncoded(barriers+" "+pedestals);
            log.debug(Arrays.toString(path));
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event){
        if (VarInfo.VARPLAYER_ID == event.getVarpId()){
            barroniteCount = event.getValue();
            if (config.showBarroniteInfoBox()){
                addBarroniteInfoBox(barroniteCount);
            }
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event){

        if (barroniteCount == -1 && event.getType() == ChatMessageType.GAMEMESSAGE){
            String message = event.getMessage().replaceAll("[0-9]","").replace(",","");
            String m1 = "You currently have  Barronite shard stored";
            String m2 = "You currently have  Barronite shards stored";

            if (message.equals(m1) || message.equals(m2)){
                barroniteCount = client.getVarpValue(VarInfo.VARPLAYER_ID);
                addBarroniteInfoBox(barroniteCount);
            }
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event){
        String key = event.getKey();
        if (key.equals("showBarroniteInfoBox")){
            if (config.showBarroniteInfoBox()){
                addBarroniteInfoBox(barroniteCount);
            }
            else{
                removeBarroniteInfoBox();
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        int region = client.getLocalPlayer().getWorldLocation().getRegionID();
        int currX = client.getLocalPlayer().getWorldLocation().getX();
        int currY = client.getLocalPlayer().getWorldLocation().getY();

        if (region == 11867){
            if (objectNumbers.isEmpty()){
                buildObjectNumbers();
            }

            if (!needToReset)
            {
                getObjects();
                detectObjectStates();
                prioritizeElaborate = config.swapLockboxPrioritizationMode();
                needToReset = true;

                convertObjStates(objectStates.toString());
                fullPath = getBestPathEncoded(barriers+" "+pedestals);

                pathUniqueNodes = Arrays.stream(fullPath).distinct().toArray(String[]::new);
                loadNext();

                log.debug("objectStates:{}", objectStates.toString());
                log.debug("prioritizeElaborate:{}",prioritizeElaborate);
                log.debug("fullPath:{}", Arrays.toString(fullPath));
                log.debug("SPAWN --> {}", pathNextNode);
            }

            boolean onActivationTile = false;

            for(int[] actCoord: pathNextActivationCoords.values()){
                if (currX == actCoord[0] && currY == actCoord[1]){
                    onActivationTile = true;
                    break;
                }
            }

            if(onActivationTile && !pathNextNode.equals("EXIT")){
                String preload = pathNextNode;
                pathNextIndex += 1;
                loadNext();
                String postload = pathNextNode;
                log.debug("{} --> {}", preload, postload);
            }
        }
        else if(region != 11867 && needToReset){
            resetAll();
        }

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        resetAll();
    }

    @Provides
    CamdozaalVaultHelperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CamdozaalVaultHelperConfig.class);
    }

    public void getObjects(){
        vaultObjects.clear();
        WorldView wv = client.getTopLevelWorldView();
        Scene scene = wv.getScene();
        Tile[][][] tiles = scene.getTiles();
        int plane = wv.getPlane();

        for(int x = 0; x < Constants.SCENE_SIZE; x++){
            for (int y = 0; y < Constants.SCENE_SIZE; y++){
                Tile currTile = tiles[plane][x][y];
                if (currTile != null){
                    GameObject[] currObjects = currTile.getGameObjects();
                    if(currObjects == null || currObjects[0] == null){
                        continue;
                    }

                    int currId = currObjects[0].getId();

                    if (ObjectInfo.OBJECT_ID_NICKNAMES.containsKey(currId)){
                        String objNickname = ObjectInfo.OBJECT_ID_NICKNAMES.get(currId);
                        if(!vaultObjects.containsKey(objNickname)){
                            vaultObjects.put(objNickname, currObjects[0]);
                        }
                    }
                }
            }
        }
    }

    public void detectObjectStates(){
        int count = 1;
        objectStates = new StringBuilder();
        for (int objId : ObjectInfo.OBJECT_ID_NICKNAMES.keySet())
        {
            int impId = getImpId(objId);
            if (ObjectInfo.IMPOSTOR_ID_NICKNAMES_ACTIVE.containsKey(impId)){
                objectStates.append("1");
            }
            else if(ObjectInfo.IMPOSTOR_ID_NICKNAMES_INACTIVE.containsKey(impId)){
                objectStates.append("0");
            }
            if (count > 29){
                break;
            }

            count += 1;
        }
    }

    public static String getNextCoords(String next) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CamdozaalVaultlHelperPlugin.class.getResourceAsStream("/objcoords.tsv")))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.substring(0,3).equals(next.substring(0,3))){
                    return line;
                }
            }
            return "none";
        } catch (IOException e) {
            // Catches both FileNotFoundException and general read/write errors
            throw new UncheckedIOException(e);
        }
    }

    public int getImpId(int objId)
    {
        return client.getObjectDefinition(objId).getImpostor().getId();
    }

    public void resetAll(){
        vaultObjects.clear();
        objectNumbers.clear();
        pathNextActivationCoords.clear();
        needToReset = false;
        objectStates = new StringBuilder(30);
        barriers = "";
        pedestals = "";
        fullPath = new String[]{};
        pathUniqueNodes = new String[]{};
        pathNextIndex = 1;
        pathNextNode = "EXIT";
    }

    public void loadNext(){
        pathNextNode = fullPath[pathNextIndex];
        String[] nextCoordsData = getNextCoords(pathNextNode).split("\t");
        int nextX = Integer.parseInt(nextCoordsData[1].split(" ")[0]);
        int nextY = Integer.parseInt(nextCoordsData[1].split(" ")[1]);
        pathNextActivationCoords.clear();

        for (int i = 2; i < nextCoordsData.length; i++){
            int activationX = Integer.parseInt(nextCoordsData[i].split(" ")[0]);
            int activationY = Integer.parseInt(nextCoordsData[i].split(" ")[1]);
            int[] currCoords = {activationX, activationY};
            pathNextActivationCoords.put(i-1, currCoords);
        }
    }

    public void addBarroniteInfoBox(int barroniteCount){
        removeBarroniteInfoBox();
        infoBox = new BarroniteInfoBox(itemManager.getImage(ItemID.CAMDOZAAL_BARRONITE_SHARD_7), this, barroniteCount);
        infoBoxManager.addInfoBox(infoBox);
    }

    public void removeBarroniteInfoBox(){
        infoBoxManager.removeInfoBox(infoBox);
    }

    public void convertObjStates(String states){
        int[] groupLengths = {2,2,3,3,2,3,2};
        int start = 0;
        int count = 1;
        barriers = "";
        pedestals = "";

        for (int length: groupLengths) {
            if (length == 2) {
                barriers = (states.substring(start, start + length).equals("01")) ? barriers + String.valueOf(count) : barriers;
                count = (count != 9) ? count + 1 : 0;
            } else {
                barriers = (states.substring(start, start + length).equals("010")) ? barriers + String.valueOf(count) : barriers;
                count = (count != 9) ? count + 1 : 0;
                barriers = (states.substring(start, start + length).equals("001")) ? barriers + String.valueOf(count) : barriers;
                count = (count != 9) ? count + 1 : 0;
            }
            start += length;
        }

        int[] pedestalIndex = {17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        count = 1;

        for (int index: pedestalIndex){
            pedestals = (states.substring(index,index+1).equals("1")) ? pedestals + String.valueOf(count): pedestals;
            count = (count != 9) ? count + 1 : 0;
        }
    }

    public static String[] getBestPathEncoded(String states) {
        int length = states.length();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CamdozaalVaultlHelperPlugin.class.getResourceAsStream("/bestpaths.tsv")))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.substring(0,length).equals(states)){
                    return convertEncodedPath(line);
                }
            }
            return null;
        } catch (IOException e) {
            // Catches both FileNotFoundException and general read/write errors
            throw new UncheckedIOException(e);
        }
    }

    public static String[] convertEncodedPath(String path){
        String[] pathArray = path.split("\t");
        int oMaxPathIndex = Integer.parseInt(pathArray[1].strip());
        int eMaxPathIndex = Integer.parseInt(pathArray[2].strip());
        String encodedPath = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(CamdozaalVaultlHelperPlugin.class.getResourceAsStream("/uniquepaths.txt")))) {

            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                if (!prioritizeElaborate && count == oMaxPathIndex){
                    encodedPath = line.strip();
                    break;
                }
                else if (prioritizeElaborate && count == eMaxPathIndex){
                    encodedPath = line.strip();
                    break;
                }
                count += 1;
            }
        } catch (IOException e) {
            // Catches both FileNotFoundException and general read/write errors
            throw new UncheckedIOException(e);
        }

        BigInteger bigIntPath = new BigInteger(encodedPath,16);
        String longPath = bigIntPath.toString();
        List<String> decimalArray = Lists.newArrayList(Splitter.fixedLength(2).split(longPath));
        String decodedPath = "SPAWN ";

        for (String objIndex: decimalArray){
            if (!objIndex.equals("40")){
                decodedPath = decodedPath.concat(objectNumbers.get(objIndex) + " ");
            }
            else{
                decodedPath = decodedPath.concat(objectNumbers.get(objIndex));
            }
        }
        return decodedPath.split(" ");

    }

    public void buildObjectNumbers(){
        int index = 1;
        int index2 = 1;
        for (int count = 10; count < 41; count++){

            if (count < 27){
                String strIndex = Integer.toString(index);
                strIndex = (strIndex.length() == 1) ? "0" + strIndex : strIndex;
                objectNumbers.put(Integer.toString(count), "B"+strIndex);
                index += 1;
            }
            else if (count < 40){
                String strIndex = Integer.toString(index2);
                strIndex = (strIndex.length() == 1) ? "0" + strIndex : strIndex;
                objectNumbers.put(Integer.toString(count), "P"+strIndex);
                index2 += 1;
            }
            else{
                objectNumbers.put(Integer.toString(count), "EXIT");
            }
        }
    }


}

