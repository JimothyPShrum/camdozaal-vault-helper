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

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

public class BarroniteInfoBox extends InfoBox{
    private final int barroniteCount;

    public BarroniteInfoBox(BufferedImage image, @Nonnull Plugin plugin, int barroniteCount)
    {
        super(image, plugin);
        this.barroniteCount = barroniteCount;
        int numVaultRuns = barroniteCount/750;
        String strVaultRuns = Integer.toString((numVaultRuns));
        String tooltip = "Barronite shards currently stored inside forge.</br>";
        if (numVaultRuns == 1){
            tooltip = tooltip + "You have enough shards for 1 vault run.";

        }
        else if(numVaultRuns == 0){
            tooltip = tooltip + "You need a minimum of 750 shards stored to enter the vault.";
        }
        else{
            tooltip = tooltip + "You have enough shards for " + strVaultRuns +" vault runs.";
        }
        setTooltip(tooltip);
    }

    @Override
    public String getText()
    {
        if (barroniteCount != -1){
            if (barroniteCount > 999999){
                int numMil = barroniteCount / 1000000;
                int twoDecimalK = (barroniteCount % 1000000) / 10000;

                if (twoDecimalK < 10){
                    return Integer.toString(numMil) + ".0" + Integer.toString(twoDecimalK) + "M";
                }
                return Integer.toString(numMil) + "." + Integer.toString(twoDecimalK) + "M";
            }
            else if (barroniteCount > 99999){
                int numK = barroniteCount / 1000;
                return Integer.toString(numK) + "K";
            }
            return Integer.toString(barroniteCount);
        }

        return "?";

    }

    @Override
    public Color getTextColor()
    {
        return Color.WHITE;
    }

}
