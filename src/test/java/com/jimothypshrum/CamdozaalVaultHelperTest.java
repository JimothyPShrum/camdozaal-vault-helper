package com.jimothypshrum;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CamdozaalVaultHelperTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CamdozaalVaultHelperPlugin.class);
		RuneLite.main(args);
	}
}