package net.runelite.client.plugins.microbot.woodcutting.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Skill;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

@Getter
@RequiredArgsConstructor
public enum WoodcuttingTree {
    TREE("tree", "Logs",1, "Chop down", true),
    OAK("oak tree", "Oak logs",15, "Chop down", true),
    WILLOW("willow tree", "Willow logs",30, "Chop down", true),
    TEAK_TREE("teak tree", "Teak logs",35, "Chop down", true),
    MAPLE("maple tree", "Maple logs",45, "Chop down", true),
    HOLLOW_TREE("hollow tree", "bark", 45, "Chop down", false),
    MAHOGANY("mahogany tree", "Mahogany logs",50, "Chop down", true),
    YEW("yew tree", "Yew logs",60, "Chop down", true),
    MAGIC("magic tree", "Magic logs",75, "Chop down", true),
    REDWOOD("redwood tree", "Redwood logs",90, "Cut", true);


    private final String name;
    private final String log;
    private final int woodcuttingLevel;
    private final String action;
    private final boolean burnable;

    @Override
    public String toString() {
        return name;
    }

    public boolean hasRequiredLevel() {
        return Rs2Player.getSkillRequirement(Skill.WOODCUTTING, this.woodcuttingLevel);
    }
}
