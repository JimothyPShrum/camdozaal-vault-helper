package com.jimothypshrum;

import net.runelite.api.gameval.ObjectID;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.*;


import javax.inject.Inject;
import java.awt.*;



public class CamdozaalVaultHelperOverlay extends Overlay
{

    private final Client client;
    private final CamdozaalVaultlHelperPlugin plugin;
    private final CamdozaalVaultHelperConfig config;

    @Inject
    private CamdozaalVaultHelperOverlay(Client client, CamdozaalVaultlHelperPlugin plugin, CamdozaalVaultHelperConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    //@Override
    public Dimension render(Graphics2D graphics)
    {
        for (String objName : plugin.getPathUniqueNodes())
        {
            if (!objName.equals("SPAWN")){
                GameObject currObj = plugin.getVaultObjects().get(objName);
                int currId = currObj.getId();
                int currImp = ObjectID.CAMDOZAAL_VAULT_DOOR;

                if (!objName.equals("EXIT")){
                    currImp = client.getObjectDefinition(currId).getImpostor().getId();
                }

                if (!ObjectInfo.IMPOSTOR_ID_NICKNAMES_INACTIVE.containsKey(currImp)){
                    final Shape polygon = currObj.getConvexHull();

                    if (polygon != null){
                        if (objName.equals(plugin.getPathNextNode())){
                            OverlayUtil.renderPolygon(graphics, polygon, config.changeNextObjectColor());
                        }

                        else {
                            OverlayUtil.renderPolygon(graphics, polygon, config.changeFullRouteColor());
                        }
                    }
                }
            }
        }
        return null;
    }
}