package net.runelite.client.plugins.microbot.logouttimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("LogoutTimer")
public interface LogoutTimerConfig extends Config {

    @ConfigItem(
            keyName = "time",
            name = "Time",
            description = "Time in minutes",
            position = 0
    )

    default int time() {
        return 30;
    }
}
