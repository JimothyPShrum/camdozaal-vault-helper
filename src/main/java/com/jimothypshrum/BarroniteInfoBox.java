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
        String tooltip = "Current barronite shards inside forge.</br>";
        if (numVaultRuns == 1){
            tooltip = tooltip + "You have enough shards for 1 vault run.";

        }
        else if(numVaultRuns == 0){
            tooltip = tooltip + "You need minimum 750 shards in the forge to enter the vault.";
        }
        else{
            tooltip = tooltip + "You have enough shards for " + strVaultRuns+" vault runs.";
        }
        setTooltip(tooltip);
    }

    @Override
    public String getText()
    {
        if (barroniteCount != -1){
            return Integer.toString(barroniteCount);
        }

        return "?";

    }

    @Override
    public Color getTextColor()
    {
        if (barroniteCount > 749){
            return Color.WHITE;
        }
        return Color.RED;
    }

}
