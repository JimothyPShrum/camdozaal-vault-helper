/*
 * Copyright (c) 2026, JimothyPShrum <https://github.com/JimothyPShrum>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jimothypshrum;

import com.google.common.collect.Lists;
import com.google.inject.Provides;
import com.google.common.base.Splitter;
import javax.inject.Inject;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.events.ConfigChanged;
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


public class CamdozaalVaultHelperPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private CamdozaalVaultHelperConfig config;

    @Inject
    private CamdozaalVaultHelperOverlay camdozaalVaultHelperOverlay;

    @Inject
    private RouteInfoOverlay routeInfoOverlay;
    @Inject
    private VaultTimerOverlay vaultTimerOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Getter
    private HashMap<String, GameObject> vaultObjects = new HashMap<>();
    private String barriers = "";
    private String pedestals = "";
    @Getter
    private int sCount = 0;
    @Getter
    private int eCount = 0;
    @Getter
    private int oCount = 0;
    @Getter
    private int routeLength = 0;
    private boolean prioritizeElaborate = false;
    private boolean needToReset = false;
    private boolean stateChanged = false;
    private String[] fullRoute = {};
    @Getter
    private String[] routeUniqueNodes = {};
    private int routeNextIndex = 1;
    @Getter
    private String routeNextNode = "EXIT";
    private HashMap<Integer, int[]> routeNextActivationCoords = new HashMap<>();
    private GameState prevGameState = GameState.LOGGING_IN;

    private BarroniteInfoBox infoBox;
    private int barroniteCount = -1;
    private int region = -1;
    private int previousRegion = -1;
    private boolean showInfoBox = false;
    private final Set<Integer> REGIONS = Set.of(11610, 11611, 11866, 11867, 12122);

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(camdozaalVaultHelperOverlay);
        overlayManager.add(routeInfoOverlay);
        overlayManager.add(vaultTimerOverlay);

        if (config.showBarroniteInfoBox()){
            clientThread.invokeLater(() -> {
                if (!(client.getGameState() == GameState.LOGGED_IN || client.getGameState() == GameState.LOADING)) {
                    showInfoBox = true;
                    return;
                }
                if (REGIONS.contains(client.getLocalPlayer().getWorldLocation().getRegionID())){
                    addBarroniteInfoBox(barroniteCount);
                }
            });
        }


    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(camdozaalVaultHelperOverlay);
        overlayManager.remove(routeInfoOverlay);
        overlayManager.remove(vaultTimerOverlay);
        removeBarroniteInfoBox();
        infoBox = null;
        barroniteCount = -1;
        region = -1;
        previousRegion = -1;
        showInfoBox = false;
        resetAll();

        final Widget timer = client.getWidget(InterfaceID.CamdozaalVault.TIMER);

        if (timer != null){
            timer.setHidden(false);
        }
    }

    //update infobox if stored barronite count is updated
    @Subscribe
    public void onVarbitChanged(VarbitChanged event){
        if (VarInfo.VARPLAYER_ID == event.getVarpId()) {
            barroniteCount = event.getValue();
            if (config.showBarroniteInfoBox() && REGIONS.contains(region)) {
                addBarroniteInfoBox(barroniteCount);
            }
        }
    }

    //update infobox if player checks vault for amount of barronite stored
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

        if (key.equals("showBarroniteInfoBox") && routeLength == 0){
            if (config.showBarroniteInfoBox() && REGIONS.contains(region)){
                addBarroniteInfoBox(barroniteCount);
            }
            else if (!config.showBarroniteInfoBox()){
                removeBarroniteInfoBox();
            }
        }
    }

    //Hide base game vault timer since it was getting in the way and haven't found how to relocate it easily.
    //VaultTimerOverlay replaces it with textbox and is mostly equivalent
    @Subscribe
    public void onBeforeRender(BeforeRender event){
        final Widget timer = client.getWidget(InterfaceID.CamdozaalVault.TIMER);

        if (timer != null){
            timer.setHidden(true);
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        region = client.getLocalPlayer().getWorldLocation().getRegionID();
        int currX = client.getLocalPlayer().getWorldLocation().getX();
        int currY = client.getLocalPlayer().getWorldLocation().getY();

        //vault is entirely contained in region 11867. only run main code when player is inside vault
        if (region == 11867){

            //determine barrier/pedestal states upon vault entry
            if (!needToReset)
            {
                log.debug("initialize");

                if (config.showBarroniteInfoBox()){
                    removeBarroniteInfoBox();
                }

                detectStates();
                prioritizeElaborate = config.swapLockboxPrioritizationMode() == CamdozaalVaultHelperConfig.LockboxPrioritizationMode.ELABORATE;
                needToReset = true;

                fullRoute = getBestRouteEncoded(barriers+" "+pedestals);

                for (String node: fullRoute){
                    if (node.charAt(0) == 'P'){
                        int pNum = Integer.parseInt(node.substring(1));

                        if (pNum < 7){ sCount += 1;}
                        else if (pNum < 11){ eCount += 1;}
                        else { oCount += 1;}
                    }
                }

                routeUniqueNodes = Arrays.stream(fullRoute).distinct().toArray(String[]::new);
                loadNext();

                log.debug("fullRoute:{}", Arrays.toString(fullRoute));
                log.debug("SPAWN --> {}", routeNextNode);
            }

            //reacquire barrier/lockbox GameObjects if game enters LOADING GameState during run
            if (stateChanged && client.getGameState() == GameState.LOGGED_IN){
                getObjects();
                stateChanged = false;
                log.debug("getting objects again after game state change");
            }

            //determine next object in route and its activation tiles if player's current tile is on one of the
            //activation tiles for current next object
            for(int[] actCoord: routeNextActivationCoords.values()){
                if (currX == actCoord[0] && currY == actCoord[1] && !routeNextNode.equals("EXIT")){
                    String before = routeNextNode;
                    routeNextIndex += 1;
                    loadNext();
                    String after = routeNextNode;
                    log.debug("{} --> {}", before, after);
                    break;
                }
            }
        }

        //reset all vault variables once outside the vault
        else if(region != 11867 && needToReset){
            resetAll();
            if (config.showBarroniteInfoBox()){
                addBarroniteInfoBox(barroniteCount);
            }
        }

        //Add infobox after entering Camdozaal
        if (REGIONS.contains(region) && !REGIONS.contains(previousRegion) && config.showBarroniteInfoBox() && previousRegion != -1){
            addBarroniteInfoBox(barroniteCount);
        }

        //Remove infobox after leaving Camdozaal
        if (!REGIONS.contains(region) && REGIONS.contains(previousRegion) && config.showBarroniteInfoBox() && previousRegion != -1){
            removeBarroniteInfoBox();
        }

        //Add infobox once game loads if plugin was turned on while logged out
        if (REGIONS.contains(region) && showInfoBox){
            showInfoBox = false;
            addBarroniteInfoBox(barroniteCount);
        }

        previousRegion = region;
    }

    //checks for a GameState change from LOGGED_IN to LOADING while inside vault to signal to reacquire vault objects
    //once GameState LOGGED_IN is reached again
    @Subscribe
    public void onGameStateChanged(GameStateChanged event){
        if (event.getGameState() == GameState.LOADING && prevGameState == GameState.LOGGED_IN && needToReset){
            stateChanged = true;
        }
        prevGameState = event.getGameState();
    }

    @Provides
    CamdozaalVaultHelperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CamdozaalVaultHelperConfig.class);
    }

    //search all tiles in scene for objects matching obj ids in ObjectInfo and store in vaultObjects
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

                    if (ObjectInfo.OBJECT_ID_NICKNAME.containsKey(currId)){
                        String objNickname = ObjectInfo.OBJECT_ID_NICKNAME.get(currId);
                        if(!vaultObjects.containsKey(objNickname)){
                            vaultObjects.put(objNickname, currObjects[0]);
                        }
                    }
                }
            }
        }
    }

    /* There are 17 barriers controlled by 7 varbits and 13 lockbox pedestals controlled by 13 varbits.
    Each barrier varbit controls which barrier in a group of 2 or 3 is active. Only 1 barrier is active per group.
    May eventually change logic to check varbit values directly rather than check associated object impostor Ids.

    Default States
    varbits : vA vB vC  vD  vE vF  vG
    barriers: 10 10 100 100 10 100 10   (0 = inactive, 1 = active)

    Excluding the default states, there are 10 total additional states (01 for each the 2-groups and 010 and 001 for each the 3-groups).
    Check 1 barrier from each 2-group and 2 barriers from each 3-group.
    Searching barriers in ObjId order, add a character 0-9 to String barriers that corresponds to whichever non-default state has occurred,
    or add nothing if default
    start from 1 (ie v1 = 01 is 1, and v7 = 01 = 0)

    example:
    varbits : vA vB vC  vD  vE vF  vG
    barriers: 01 10 001 100 10 010 10
               1  2  34  56  7  89  0
    barriers = 1      4         8     --> "148"

    Each Lockbox is controlled by its own varbit. All 3 ornate lockboxes always spawn, so no need to check for them
    10 lockboxes to check for (6 simple, 4 elaborate). ObjId order has all 6 Simples first followed by 4 Elaborates

    Check 10 lockbox objects and add character 0-9 (starting with 1) to String pedestalsthat corresponds
    to lockbox number if it has spawned, or add nothing if not spawned

    example:
    Lockbox  : SSSXSXEEXE (S/E = spawned Simple/Elaborate, X = unspawned)
    pedestals: 123 5 78 0 --> "1235780"

    encode strings barriers and pedestals to base 36 to eventually search for corresponding route in bestroutes.tsv
    */

    public void detectStates(){
        getObjects();
        log.debug("got objects");
        barriers = "";
        pedestals = "";

        for (Map.Entry<String, String> entry: ObjectInfo.STATE_OBJECT_CONVERSIONS.entrySet()){
            String currObjName = entry.getKey();
            String currObjConversion = entry.getValue();
            int currImp = getImpId(ObjectInfo.NICKNAME_OBJECT_ID.get(currObjName));

            if (currObjName.charAt(0) == 'B'){
                barriers = (ObjectInfo.IMPOSTOR_ID_NICKNAME_ACTIVE.containsKey(currImp)) ? barriers + currObjConversion : barriers;
            }
            else if (currObjName.charAt(0) == 'P'){
                pedestals = (ObjectInfo.IMPOSTOR_ID_NICKNAME_ACTIVE.containsKey(currImp)) ? pedestals + currObjConversion : pedestals;
            }
        }

        log.debug("barriers before: {}", barriers);
        log.debug("pedestals before: {}", pedestals);

        barriers = (!barriers.isEmpty()) ? Integer.toString(Integer.parseInt(barriers),36).toUpperCase() : barriers;
        pedestals = (!pedestals.isEmpty()) ? Integer.toString(Integer.parseInt(pedestals),36).toUpperCase() : pedestals;
        log.debug("barriers: {}", barriers);
        log.debug("pedestals: {}", pedestals);
    }

    /* objcoords.tsv has 6 columns. some rows only use 4 columns. all coordinates are "x y" format.
    2nd column is the tile coordinate of the object itself and is currently not used.
    activation tiles are tiles where player can pass through barrier or take lockbox. barriers have 4 act tiles (2 for each side).
    pedestals only have 2 act tiles

     col1                                     col2       col3       col4       col5       col6
     objNickname ie(B01-B17, P01-P13, EXIT) | objCoord | actTile1 | actTile2 | actTile3 | actTile4

     searches each line in objcoords.tsv for the line starting with next obj nickname and returns the entire line
     */

    public String getNextCoords(String next) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CamdozaalVaultHelperPlugin.class.getResourceAsStream("/objcoords.tsv")))) {

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

    //return impostor id of passed obj id. all obj ids passed to this method are guaranteed to have an impostor
    public int getImpId(int objId)
    {
        return client.getObjectDefinition(objId).getImpostor().getId();
    }

    public void resetAll(){
        log.debug("resetting");
        vaultObjects.clear();
        routeNextActivationCoords.clear();
        needToReset = false;
        barriers = "";
        pedestals = "";
        sCount = 0;
        eCount = 0;
        oCount = 0;
        routeLength = 0;
        fullRoute = new String[]{};
        routeUniqueNodes = new String[]{};
        routeNextIndex = 1;
        routeNextNode = "EXIT";
    }

    // get the next object in the route and its activation tile coordinates
    public void loadNext(){
        log.debug("loading next");
        routeNextNode = fullRoute[routeNextIndex];
        String[] nextCoordsData = getNextCoords(routeNextNode).split("\t");
        routeNextActivationCoords.clear();

        for (int i = 2; i < nextCoordsData.length; i++){
            int activationX = Integer.parseInt(nextCoordsData[i].split(" ")[0]);
            int activationY = Integer.parseInt(nextCoordsData[i].split(" ")[1]);
            int[] nextCoords = {activationX, activationY};
            routeNextActivationCoords.put(i-1, nextCoords);
        }
    }

    public void addBarroniteInfoBox(int barroniteCount){
        removeBarroniteInfoBox();
        log.debug("adding infobox");
        infoBox = new BarroniteInfoBox(itemManager.getImage(ItemID.CAMDOZAAL_BARRONITE_SHARD_5), this, barroniteCount);
        infoBoxManager.addInfoBox(infoBox);
    }

    public void removeBarroniteInfoBox(){
        log.debug("removing infobox");
        infoBoxManager.removeInfoBox(infoBox);
    }

    /* bestroutes.tsv is formatted as 3 columns. col1 is formatted as explained in detectStates() comments.
    index numbers refer to row number in uniqueroutes.tsv

    col1               | col2                                                | col3
    barriers pedestals | row index for max Ornate lockbox route (in base 36) | row index for max Elaborate lockbox route (in base 36)

    search bestroutes.tsv for row starting with barriers + " " + pedestals that were previously determined in detectStates()
    return decoded route as String[] (decoding done in convertEncodedRoute)

     */
    public String[] getBestRouteEncoded(String states) {
        int length = states.length();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(CamdozaalVaultHelperPlugin.class.getResourceAsStream("/bestroutes.tsv")))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() < length + 1) {continue;}
                if (line.substring(0,length+1).equals(states+"\t")){
                    return convertEncodedRoute(line);
                }
            }
            return null;
        } catch (IOException e) {
            // Catches both FileNotFoundException and general read/write errors
            throw new UncheckedIOException(e);
        }
    }
    /* uniqueroutes.tsv is formatted with 2 columns

    col1                                                                       | col2
    double encoded route (base 36, then numbers corresponding to obj Nicknames) | route length (in ticks)

    passed string is a line from bestroutes.tsv (encoded obj states | max ornate route index | max elaborate route index)

    convert indexes from base36 to decimal then iterate through uniqueroutes.tsv to the appropriate index determined
    by value of prioritizeElaborate variable controlled by config

    convert uniqueroute col1 value to decimal then split into array of 2 digit strings that correspond to object nicknames
    as defined in ObjectInfo

    return fully decoded array of full route
     */
    public String[] convertEncodedRoute(String route){
        String[] routeArray = route.split("\t");
        int oMaxRouteIndex = Integer.parseInt(routeArray[1].strip(),36);
        int eMaxRouteIndex = Integer.parseInt(routeArray[2].strip(),36);
        String encodedRoute = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(CamdozaalVaultHelperPlugin.class.getResourceAsStream("/uniqueroutes.tsv")))) {

            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                if (!prioritizeElaborate && count == oMaxRouteIndex){
                    String[] routeData = line.strip().split("\t");
                    encodedRoute = routeData[0];
                    routeLength = Integer.parseInt(routeData[1]);
                    break;
                }
                else if (prioritizeElaborate && count == eMaxRouteIndex){
                    String[] routeData = line.strip().split("\t");
                    encodedRoute = routeData[0];
                    routeLength = Integer.parseInt(routeData[1]);
                    break;
                }
                count += 1;
            }
        } catch (IOException e) {
            // Catches both FileNotFoundException and general read/write errors
            throw new UncheckedIOException(e);
        }

        BigInteger bigIntRoute = new BigInteger(encodedRoute,36);
        String strRoute = bigIntRoute.toString();
        List<String> decimalArray = Lists.newArrayList(Splitter.fixedLength(2).split(strRoute));
        String decodedRoute = "SPAWN ";

        for (String objIndex: decimalArray){
            decodedRoute = (!objIndex.equals("40")) ? decodedRoute.concat(ObjectInfo.ROUTE_OBJECT_NUMBERS.get(objIndex) + " ")
                    : decodedRoute.concat(ObjectInfo.ROUTE_OBJECT_NUMBERS.get(objIndex));
        }
        return decodedRoute.split(" ");

    }


}

