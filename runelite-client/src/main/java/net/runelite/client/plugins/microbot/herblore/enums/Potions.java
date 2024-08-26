package net.runelite.client.plugins.microbot.herblore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Potions {
    ATTACK("attack potion", Arrays.asList(UnfinishedPotions.GUAM_POTION.getName(), "eye of newt"), 3),
    ANTIPOISON("antipoison", Arrays.asList(UnfinishedPotions.MARRENTILL_POTION.getName(), "unicorn horn dust"), 5),
    STRENGTH("strength potion", Arrays.asList(UnfinishedPotions.TARROMIN_POTION.getName(), "limpwurt root"), 12),
    COMPOST("compost potion", Arrays.asList(UnfinishedPotions.HARRALANDER_POTION.getName(), "volcanic ash"), 22),
    RESTORE("restore potion", Arrays.asList(UnfinishedPotions.HARRALANDER_POTION.getName(), "red spiders' eggs"), 22),
    ENERGY("energy potion", Arrays.asList(UnfinishedPotions.HARRALANDER_POTION.getName(), "chocolate dust"), 26),
    DEFENCE("defence potion", Arrays.asList(UnfinishedPotions.RANARR_POTION.getName(), "white berries"), 30),
    AGILITY("agility potion", Arrays.asList(UnfinishedPotions.TOADFLAX_POTION.getName(), "toad's legs"), 34),
    COMBAT("combat potion", Arrays.asList(UnfinishedPotions.HARRALANDER_POTION.getName(), "goat horn dust"), 36),
    PRAYER("prayer potion", Arrays.asList(UnfinishedPotions.RANARR_POTION.getName(), "snape grass"), 38),
    SUPER_ATTACK("super attack", Arrays.asList(UnfinishedPotions.IRIT_POTION.getName(), "eye of newt"), 45),
    SUPERANTIPOISON("superantipoison", Arrays.asList(UnfinishedPotions.IRIT_POTION.getName(), "unicorn horn dust"),48),
    FISHING("fishing potion", Arrays.asList(UnfinishedPotions.AVANTOE_POTION.getName(), "snape grass"), 50),
    SUPER_ENERGY("super energy", Arrays.asList(UnfinishedPotions.AVANTOE_POTION.getName(), "mort myre fungus"), 52),
    HUNTER("hunter potion", Arrays.asList(UnfinishedPotions.AVANTOE_POTION.getName(), "kebbit teeth dust"), 53),
    SUPER_STRENGTH("super strength", Arrays.asList(UnfinishedPotions.KWUARM_POTION.getName(), "limpwurt root"), 55),
    WEAPON_POISON("weapon poison", Arrays.asList(UnfinishedPotions.KWUARM_POTION.getName(), "dragon scale dust"), 60),
    SUPER_RESTORE("super restore", Arrays.asList(UnfinishedPotions.SNAPDRAGON_POTION.getName(), "red spiders' eggs"), 63),
    SUPER_DEFENCE("super defence", Arrays.asList(UnfinishedPotions.CADANTINE_POTION.getName(), "white berries"), 66),
    ANTIDOTE_PLUS("antidote+", Arrays.asList(UnfinishedPotions.TOADFLAX_POTION.getName(), "yew roots"), 68),
    ANTIFIRE("antifire potion", Arrays.asList(UnfinishedPotions.LANTADYME_POTION.getName(), "dragon scale dust"), 69),
    RANGING("ranging potion", Arrays.asList(UnfinishedPotions.DWARF_POTION.getName(), "wine of zamorak"), 72),
    MAGIC("magic potion", Arrays.asList(UnfinishedPotions.LANTADYME_POTION.getName(), "potato cactus"), 76),
    ZAMORAK("zamorak brew", Arrays.asList(UnfinishedPotions.TORSTOL_POTION.getName(), "jangerberries"), 78),
    SARADOMIN("saradomin brew", Arrays.asList(UnfinishedPotions.TOADFLAX_POTION.getName(), "crushed nest"), 81),
    ANCIENT("ancient brew", Arrays.asList(UnfinishedPotions.DWARF_POTION.getName(), "nihil dust"), 85),
    MENAPHITE("menaphite remedy", Arrays.asList(UnfinishedPotions.DWARF_POTION.getName(), "lily of the sands"), 88);

    private final String name;
    private final List<String> ingredients;
    private final int levelRequired;
}
