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

import java.awt.*;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;


public class VaultTimerOverlay extends OverlayPanel{
    private final Client client;
    private final CamdozaalVaultHelperPlugin plugin;
    private final CamdozaalVaultHelperConfig config;
    private int onClientTick = 0;

    @Inject
    private VaultTimerOverlay(CamdozaalVaultHelperPlugin plugin, CamdozaalVaultHelperConfig config, Client client){
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(PRIORITY_LOW);
        setMovable(true);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (plugin.getRouteLength() == 0){return null;}

        onClientTick = (onClientTick == 9) ? 0 : onClientTick + 1;

        int timeRemaining = (client.getVarbitValue(VarInfo.TIME_REMAINING_VARBIT_ID)-1) * 6 / 10;

        String strTime = (timeRemaining > 60) ? "60" : Integer.toString(timeRemaining);
        Color timeColor;

        if (timeRemaining > 10 || onClientTick < 5){
            timeColor = Color.WHITE;
        }
        else {
            timeColor = Color.RED;
        }

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Time Remaining:")
                .right(strTime)
                .rightColor(timeColor)
                .build());

        return super.render(graphics);
    }
}
