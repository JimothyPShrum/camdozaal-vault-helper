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

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class RouteInfoOverlay extends OverlayPanel{

    private final CamdozaalVaultHelperPlugin plugin;
    private final CamdozaalVaultHelperConfig config;

    @Inject
    private RouteInfoOverlay(CamdozaalVaultHelperPlugin plugin, CamdozaalVaultHelperConfig config){
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(PRIORITY_LOW);
        setMovable(true);
        this.plugin = plugin;
        this.config = config;
    }
    @Override
    public Dimension render(Graphics2D graphics)
    {
        int routeLength = plugin.getRouteLength();
        if (!config.showRouteTextBox() ||
                routeLength == 0)
        {
            return null;
        }

        //convert from ticks to seconds
        String routeSeconds = Integer.toString((routeLength * 6) / 10);
        String routeMSeconds = Integer.toString((routeLength * 6) % 10);
        String routeTime = (routeMSeconds.equals("0")) ? routeSeconds + "s" : routeSeconds + "." + routeMSeconds + "s";
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Duration:")
                .right(routeTime)
                .build());


        String risk = "LOW";
        Color riskColor = Color.WHITE;
        if (routeLength > 96){
            risk = "HIGH";
            riskColor = Color.RED;
        }
        else if (routeLength > 93) {
            risk = "MED";
            riskColor = Color.ORANGE;
        }
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Risk If Delayed:")
                .right(risk)
                .rightColor(riskColor)
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Simple:")
                .right(Integer.toString(plugin.getSCount()))
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Elaborate:")
                .right(Integer.toString(plugin.getECount()))
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Ornate:")
                .right(Integer.toString(plugin.getOCount()))
                .build());

        return super.render(graphics);
    }

}
