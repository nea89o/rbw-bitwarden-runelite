package moe.nea.rbwrl;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class UnofficialBitwardenCliPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(UnofficialBitwardenCliPlugin.class);
		RuneLite.main(args);
	}
}