package com.jimothypshrum;

import net.runelite.client.config.*;

import java.awt.*;


@ConfigGroup(CamdozaalVaultHelperConfig.GROUP)
public interface CamdozaalVaultHelperConfig extends Config {
	String GROUP = "camdozaalvaulthelper";
	@ConfigSection(
			name = "Lockbox Prioritization",
			description = "Choose lockbox type to prioritize",
			position = 0
	)
	String lockboxPrioritizationSection = "lockboxPrioritizationSection";


	@ConfigItem(
			position = 0,
			keyName = "swapLockboxPrioritizationMode",
			name = "Prioritize elaborate",
			description = "Check to prioritize elaborate lockboxes. Uncheck to prioritize ornate lockboxes.",
			section = lockboxPrioritizationSection
	)
	default boolean swapLockboxPrioritizationMode() {
		return false;
	}

	@ConfigSection(
			name = "Route Highlighting",
			description = "Choose colors to highlight all objects in route and next object in route",
			position = 1
	)
	String routeHighlightingSection = "routeHighlightingSection";

	@Alpha
	@ConfigItem(
			position = 0,
			keyName = "changeFullRouteColor",
			name = "Full route",
			description = "Change which color to highlight all objects in route.",
			section = routeHighlightingSection
	)
	default Color changeFullRouteColor() {
		return java.awt.Color.YELLOW;
	}

	@Alpha
	@ConfigItem(
			position = 1,
			keyName = "changeNextObjectColor",
			name = "Next object",
			description = "Change which color to highlight the next object in route.",
			section = routeHighlightingSection
	)
	default Color changeNextObjectColor() {
		return java.awt.Color.CYAN;
	}

	@ConfigSection(
			name = "Show Barronite InfoBox",
			description = "Choose whether to display barronite count InfoBox",
			position = 2
	)
	String showBarroniteInfoBoxSection = "showBarroniteInfoBoxSection";

	@ConfigItem(
			position = 0,
			keyName = "showBarroniteInfoBox",
			name = "Show Barronite InfoBox",
			description = "Check to show InfoBox with current barronite shards in vault.",
			section = showBarroniteInfoBoxSection
	)
	default boolean showBarroniteInfoBox() {
		return true;
	}


}
