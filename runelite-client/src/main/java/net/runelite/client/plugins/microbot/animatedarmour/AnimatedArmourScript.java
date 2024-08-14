package net.runelite.client.plugins.microbot.animatedarmour;

import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.animatedarmour.enums.PlateType;
import net.runelite.client.plugins.microbot.animatedarmour.enums.State;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.LootingParameters;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

public class AnimatedArmourScript extends Script {
    public static double version = 1.0;

    public static State state;
    public static PlateType plate;
    public static SplittableRandom random = new SplittableRandom();

    public boolean run(AnimatedArmourConfig config) {
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

                TileObject animator = Rs2GameObject.findGameObjectByLocation(new WorldPoint(2857, 3536, 0));
                WorldPoint bank = new WorldPoint(2843, 3541, 0);
                WorldPoint room = new WorldPoint(2857, 3539, 0);
                plate = config.plate();

                boolean hasPlateSet = Rs2Inventory.containsAll(plate.getBody(), plate.getLegs(), plate.getHelm());
                boolean hasFood = !Rs2Inventory.getInventoryFood().isEmpty();

                if(!hasFood && hasPlateSet && !Rs2Combat.inCombat()) {
                    state = State.BANKING;
                }

                else if(hasFood && hasPlateSet && Rs2GameObject.hasLineOfSight(animator)) {
                    state = State.READY;
                }

                else if(!hasPlateSet && !Rs2Combat.inCombat()) {
                    state = State.LOOTING;
                }

                else {
                    state = State.WAITING;
                }

                switch(state) {
                    case BANKING:
                        while(!Rs2Walker.isInArea(bank, 2)) {
                            Rs2Walker.walkTo(bank, 2);
                            sleep(500, 1250);
                        }

                        while(!Rs2Bank.isOpen()) {
                            Rs2Bank.useBank();
                            sleep(500, 1250);
                        }

                        if (Rs2Bank.hasItem(config.food().getName())) {
                            Rs2Bank.withdrawAll(config.food().getId());
                        }

                        else {
                            Rs2Player.logout();
                        }

                        randomWait();
                        while(!Rs2Walker.isInArea(room, 2)) {
                            Rs2Walker.walkTo(room, 2);
                            sleep(500, 1250);
                        }
                        break;

                    case READY:
                        randomWait();
                        Rs2GameObject.interact(animator);
                        sleepUntil((Rs2Combat::inCombat), 5000);
                        break;

                    case LOOTING:
                        randomWait();
                        LootingParameters lootParams = new LootingParameters(
                                4,
                                1,
                                1,
                                0,
                                false,
                                false,
                                plate.getBody(),
                                plate.getLegs(),
                                plate.getHelm(),
                                "warrior guild token"
                        );

                        if(Rs2GroundItem.lootItemsBasedOnNames(lootParams)) {
                            Microbot.pauseAllScripts = false;
                        }
                        break;

                    case WAITING:
                        Rs2Player.eatAt(50);
                        sleep(1000, 2000);
                        break;
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
}

    private void randomWait() {
        int i = random.nextInt(100);
        long startTime = System.currentTimeMillis();

        if(i < 20) {
            return;
        }
        else if(i < 40) {
            sleep(250, 500);
        }
        else if(i < 60) {
            sleep(500, 1000);
        }
        else if(i < 80) {
            sleep(750, 1500);
        }
        else if(i < 95) {
            sleep(1000, 2000);
        }
        else {
            sleep(5000, 10000);
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
