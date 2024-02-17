package moe.nea.rbwrl;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("unofficialbitwardencli")
public interface UnofficialBitwardenCliConfig extends Config
{
	@ConfigItem(
		keyName = "rbwBinary",
		name = "Binary Path",
		description = "Set the path to the rbw binary here, or leave it empty to default to \"rbw\"."
	)
	default String getBinaryPath()
	{
		return "rbw";
	}
}
