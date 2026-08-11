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
    private final CamdozaalVaultHelperPlugin plugin;
    private final CamdozaalVaultHelperConfig config;

    @Inject
    private CamdozaalVaultHelperOverlay(Client client, CamdozaalVaultHelperPlugin plugin, CamdozaalVaultHelperConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        for (String objName : plugin.getRouteUniqueNodes())
        {
            //skip rendering object if it's not in scene yet
            //happens sometimes to the ornate pedestals if player enters the vault for the first time
            if (!plugin.getVaultObjects().containsKey(objName)){continue;}

            //highlight all rendered objects in path (separate color for next object)
            if (!objName.equals("SPAWN")){
                GameObject currObj = plugin.getVaultObjects().get(objName);
                int currId = currObj.getId();
                int currImp = ObjectID.CAMDOZAAL_VAULT_DOOR;

                if (!objName.equals("EXIT")){
                    currImp = client.getObjectDefinition(currId).getImpostor().getId();
                }

                if (!ObjectInfo.IMPOSTOR_ID_NICKNAME_INACTIVE.containsKey(currImp)){
                    final Shape polygon = currObj.getConvexHull();

                    if (polygon != null){
                        if (objName.equals(plugin.getRouteNextNode())){
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