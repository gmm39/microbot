package net.runelite.client.plugins.microbot.herblore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Herbs {
    GUAM("grimy guam leaf", "guam leaf", 3),
    MARRENTILL("grimy marrentill", "marrentill", 5),
    TARROMIN("grimy tarromin", "tarromin", 11),
    HARRALANDER("grimy harralander", "harralander", 20),
    RANARR("grimy ranarr weed", "ranarr weed", 25),
    TOADFLAX("grimy toadflax", "toadflax", 30),
    IRIT("grimy irit leaf", "irit leaf", 40),
    AVANTOE("grimy avantoe", "avantoe", 48),
    KWUARM("grimy kwuarm", "kwuarm", 54),
    SNAPDRAGON("grimy snapdragon", "snapdragon", 59),
    CADANTINE("grimy cadantine", "cadantine", 65),
    LANTADYME("grimy lantadyme", "lantadyme", 67),
    DWARF("grimy dwarf weed", "dwarf weed", 70),
    TORSTOL("grimy torstol", "torstol", 75);

    private final String grimyName;
    private final String cleanName;
    private final int levelRequired;
}
