package net.runelite.client.plugins.microbot.herblore;

import net.runelite.api.Skill;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.herblore.enums.HerbloreAction;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.antiban.Rs2AntibanSettings;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class HerbloreScript extends Script {

    public static String version = "1.1";
    private static List<HerbloreAction> actions = new ArrayList<>();

    public boolean run(HerbloreConfig config) {
        Microbot.enableAutoRunOn = false;

        Rs2Antiban.antibanSetupTemplates.applyHerbloreSetup();

        if(config.cleaning()) actions.add(HerbloreAction.CLEANING);
        if(config.unfinishedPotions()) actions.add(HerbloreAction.UNFINISHED_POTIONS);
        if(config.potions()) actions.add(HerbloreAction.POTIONS);

        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!super.run() || !Microbot.isLoggedIn() ||
                        Rs2AntibanSettings.actionCooldownActive || Microbot.pauseAllScripts) return;

                if(actions.isEmpty()) {
                    shutdown();
                    return;
                }

                switch(actions.get(0)) {
                    case CLEANING:
                        cleaning(config);
                        break;

                    case UNFINISHED_POTIONS:
                        unfinishedPotions(config);
                        break;

                    case POTIONS:
                        potions(config);
                        break;

                    default:
                        shutdown();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private void cleaning(HerbloreConfig config) {
        hasLevel(config.herb().getLevelRequired());

        String grimyHerb = config.herb().getGrimyName();

        getItems(Collections.singletonList(grimyHerb));

        //Cleaning step
        do {
            Rs2Inventory.interact(grimyHerb, "clean");
            sleepUntil(() -> !Rs2Inventory.hasItem(grimyHerb) || !Microbot.isGainingExp, 35000);
        } while(Rs2Inventory.hasItem(grimyHerb));

        Rs2Antiban.actionCooldown();
        Rs2Antiban.takeMicroBreakByChance();
    }

    private void unfinishedPotions(HerbloreConfig config) {
        hasLevel(config.unfinishedPotion().getLevelRequired());

        List<String> ingredients = config.unfinishedPotion().getIngredients();

        getItems(ingredients);

        //Unfinished potion creation
        do {
            Rs2Inventory.combine(ingredients.get(0), ingredients.get(1));
            sleepUntil(() -> Rs2Widget.findWidget("How many do you wish to make?", null, false) != null);
            Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);

            sleepUntil(() -> ingredients.stream().anyMatch(x -> !Rs2Inventory.hasItem(x))
                    || !Rs2Inventory.waitForInventoryChanges(), 35000);
        } while(ingredients.stream().allMatch(Rs2Inventory::hasItem));

        Rs2Antiban.actionCooldown();
        Rs2Antiban.takeMicroBreakByChance();
    }

    private void potions(HerbloreConfig config) {
        hasLevel(config.potion().getLevelRequired());

        List<String> ingredients = config.potion().getIngredients();

        getItems(ingredients);

        //Potion creation
        do {
            Rs2Inventory.combine(ingredients.get(0), ingredients.get(1));
            sleepUntil(() -> Rs2Widget.findWidget("How many do you wish to make?", null, false) != null);
            Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);

            sleepUntil(() -> ingredients.stream().anyMatch(x -> !Rs2Inventory.hasItem(x))
                    || !Rs2Inventory.waitForInventoryChanges(), 35000);
        } while(ingredients.stream().allMatch(Rs2Inventory::hasItem));

        Rs2Antiban.actionCooldown();
        Rs2Antiban.takeMicroBreakByChance();
    }

    private void getItems(List<String> items) {
        if (!Rs2Bank.isOpen())
            Rs2Bank.useBank();
        sleepUntil(Rs2Bank::isOpen);

        if(!Rs2Inventory.isEmpty())
            Rs2Bank.depositAll();

        for(String item : items) {
            if (Rs2Bank.hasItem(item, true)) {
                Rs2Bank.withdrawX(true, item, (Rs2Inventory.capacity() / items.size()), true);
                sleep(250, 500); //buffer
            }
            else {
                actions.remove(0);
                if(actions.isEmpty()) Rs2Bank.closeBank();
                return;
            }
        }

        Rs2Bank.closeBank();
        sleepUntil(() -> !Rs2Bank.isOpen());
        sleep(250, 500); //buffer
    }

    private void hasLevel(int levelRequired) {
        if(!Rs2Player.getSkillRequirement(Skill.HERBLORE, levelRequired)) {
            Microbot.showMessage("You do not meet the herblore level requirement.");
            shutdown();
        }
    }

    @Override
    public void shutdown() {
        Rs2Antiban.resetAntibanSettings();
        super.shutdown();
    }
}
