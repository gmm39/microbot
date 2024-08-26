package net.runelite.client.plugins.microbot.herblore;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.herblore.enums.HerbloreAction;
import net.runelite.client.plugins.microbot.herblore.enums.Herbs;
import net.runelite.client.plugins.microbot.herblore.enums.Potions;
import net.runelite.client.plugins.microbot.herblore.enums.UnfinishedPotions;
import net.runelite.client.plugins.microbot.util.misc.Rs2Food;

@ConfigGroup("HerblorePlugin")
public interface HerbloreConfig extends Config {

    @ConfigSection(
            name = "General",
            description = "General Herblore Settings",
            position = 0
    )
    String generalSection = "general";

    @ConfigSection(
            name = "Cleaning",
            description = "Cleaning Settings",
            position = 1
    )
    String cleaningSection = "cleaning";

    @ConfigSection(
            name = "Unfinished Potions",
            description = "Unfinished Potion Settings",
            position = 2
    )
    String unfinishedPotionsSection = "unfinished";

    @ConfigSection(
            name = "Potions",
            description = "Potion Settings",
            position = 3
    )
    String potionsSection = "potions";

    @ConfigItem(
            name = "How to use",
            keyName = "Guide",
            description = "How to use this plugin",
            position = 0,
            section = generalSection
    )

    default String guide() {
        return "This is a bank standing semi-auto herblore script.\n" +
                "1. Select which herblore action you would like to perform.\n" +
                "2. Select select which item you would like to create from the corresponding subsection.";
    }

    @ConfigItem(
            name = "Action Type",
            keyName = "ActionType",
            description = "Type of herblore action",
            position = 1,
            section = generalSection
    )

    default HerbloreAction ActionType() {
        return HerbloreAction.CLEANING;
    }

    @ConfigItem(
            name = "Herb",
            keyName = "Herb",
            description = "Type of herb to clean",
            position = 0,
            section = cleaningSection
    )

    default Herbs herb()
    {
        return Herbs.GUAM;
    }

    @ConfigItem(
            name = "Unfinished Potion",
            keyName = "UnfinishedPotion",
            description = "Type of unfinished potion to create",
            position = 0,
            section = unfinishedPotionsSection
    )

    default UnfinishedPotions unfinishedPotion()
    {
        return UnfinishedPotions.GUAM_POTION;
    }

    @ConfigItem(
            name = "Potion",
            keyName = "Potion",
            description = "Type of potion to create",
            position = 0,
            section = potionsSection
    )

    default Potions potion()
    {
        return Potions.ATTACK;
    }
}
