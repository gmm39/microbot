package net.runelite.client.plugins.microbot.herblore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UnfinishedPotions {
    GUAM_POTION("Guam potion (unf)", Arrays.asList(Herbs.GUAM.getCleanName(), "vial of water"), 3),
    MARRENTILL_POTION("Marrentill potion (unf)", Arrays.asList(Herbs.MARRENTILL.getCleanName(), "vial of water"), 5),
    TARROMIN_POTION("Tarromin potion (unf)", Arrays.asList(Herbs.TARROMIN.getCleanName(), "vial of water"), 12),
    HARRALANDER_POTION("Harralander potion (unf)", Arrays.asList(Herbs.HARRALANDER.getCleanName(), "vial of water"), 22),
    RANARR_POTION("Ranarr potion (unf)", Arrays.asList(Herbs.RANARR.getCleanName(), "vial of water"), 30),
    TOADFLAX_POTION("Toadflax potion (unf)", Arrays.asList(Herbs.TOADFLAX.getCleanName(), "vial of water"), 34),
    IRIT_POTION("Irit potion (unf)", Arrays.asList(Herbs.IRIT.getCleanName(), "vial of water"), 45),
    AVANTOE_POTION("Avantoe potion (unf)", Arrays.asList(Herbs.AVANTOE.getCleanName(), "vial of water"), 50),
    KWUARM_POTION("Kwuarm potion (unf)", Arrays.asList(Herbs.KWUARM.getCleanName(), "vial of water"), 55),
    SNAPDRAGON_POTION("Snapdragon potion (unf)", Arrays.asList(Herbs.SNAPDRAGON.getCleanName(), "vial of water"), 63),
    CADANTINE_POTION("Cadantine potion (unf)", Arrays.asList(Herbs.CADANTINE.getCleanName(), "vial of water"), 66),
    LANTADYME_POTION("Lantadyme potion (unf)", Arrays.asList(Herbs.LANTADYME.getCleanName(), "vial of water"), 69),
    DWARF_POTION("Dwarf weed potion (unf)", Arrays.asList(Herbs.DWARF.getCleanName(), "vial of water"), 72),
    TORSTOL_POTION("Torstol potion (unf)", Arrays.asList(Herbs.TORSTOL.getCleanName(), "vial of water"), 78);

    private final String name;
    private final List<String> ingredients;
    private final int levelRequired;
}
