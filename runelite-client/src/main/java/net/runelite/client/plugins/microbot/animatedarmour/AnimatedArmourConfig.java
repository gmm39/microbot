package net.runelite.client.plugins.microbot.animatedarmour;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.microbot.animatedarmour.enums.PlateType;
import net.runelite.client.plugins.microbot.util.misc.Rs2Food;

@ConfigGroup("AnimatedArmourPlugin")
public interface AnimatedArmourConfig extends Config {

    @ConfigItem(
            keyName = "Guide",
            name = "How to use",
            description = "How to use this plugin",
            position = 0
    )

    default String guide() {
        return "4 slots for armour/tokens & 24 slots for food.";
    }

    @ConfigItem(
            keyName = "Plate",
            name = "Plate",
            description = "Type of plate",
            position = 0
    )

    default PlateType plate() {
        return PlateType.BLACK;
    }

    @ConfigItem(
            keyName = "Food",
            name = "Food",
            description = "Type of food",
            position = 1
    )

    default Rs2Food food()
    {
        return Rs2Food.SWORDFISH;
    }
}
