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

	enum LockboxPrioritizationMode{
		ORNATE,
		ELABORATE,
	}


	@ConfigItem(
			position = 0,
			keyName = "swapLockboxPrioritizationMode",
			name = "Target Lockbox Type",
			description = "Select which type of lockbox to prioritize",
			section = lockboxPrioritizationSection
	)
	default LockboxPrioritizationMode swapLockboxPrioritizationMode() {
		return LockboxPrioritizationMode.ORNATE;
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
			name = "Barronite InfoBox",
			description = "Choose whether to display barronite count InfoBox",
			position = 2
	)
	String showBarroniteInfoBoxSection = "showBarroniteInfoBoxSection";

	@ConfigItem(
			position = 0,
			keyName = "showBarroniteInfoBox",
			name = "Show Barronite InfoBox",
			description = "When outside vault, show InfoBox with current barronite shards in forge.",
			section = showBarroniteInfoBoxSection
	)
	default boolean showBarroniteInfoBox() {
		return true;
	}

	@ConfigSection(
			name = "Route Info TextBox",
			description = "Choose whether to display Route Info TextBox",
			position = 3
	)
	String showRouteTextBoxSection = "showRouteTextBoxSection";

	@ConfigItem(
			position = 0,
			keyName = "showRouteTextBox",
			name = "Show Route Info TextBox",
			description = "When in vault, show TextBox in top-left with expected time route will take to complete, risk, and number of lockboxes.",
			section = showRouteTextBoxSection
	)
	default boolean showRouteTextBox() {
		return true;
	}


}
