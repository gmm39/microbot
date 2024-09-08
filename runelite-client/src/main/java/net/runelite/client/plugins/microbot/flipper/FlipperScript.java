package net.runelite.client.plugins.microbot.flipper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.antiban.Rs2AntibanSettings;
import net.runelite.client.plugins.microbot.util.antiban.enums.ActivityIntensity;
import net.runelite.client.plugins.microbot.util.antiban.enums.Category;
import net.runelite.client.plugins.microbot.util.grandexchange.GrandExchangeSlots;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class FlipperScript extends Script {

    public static String version = "1.0";
    private static Plugin flippingCopilot;
    private static Method getCurrentSuggestion;
    private static boolean initialized = false;
    private Suggestion currentSuggestion = new Suggestion();
    private int waitCount = 0;

    public boolean run(FlipperConfig config) {
        Microbot.enableAutoRunOn = false;
        antibanSetup();

        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if(!Microbot.isLoggedIn() || !super.run()) return;
                if(Rs2AntibanSettings.actionCooldownActive || Microbot.pauseAllScripts) return;

                if(!initialized) sleepUntil(this::init);

                if(!Microbot.getPluginManager().isPluginEnabled(flippingCopilot)) return;

                flippingMain();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private boolean init() {
        if(initialized) return true;
        String pluginName = "com.flippingcopilot.controller.FlippingCopilotPlugin";

        try {
            Collection<Plugin> pls = Microbot.getPluginManager().getPlugins();
            flippingCopilot = pls.stream().filter(x -> x.getClass().getName().equals(pluginName))
                    .findFirst().orElse(null);

            if (flippingCopilot != null) {
                getCurrentSuggestion = flippingCopilot.getClass().getMethod("getCurrentSuggestion");
                initialized = true;
                return true;
            }
        } catch(Exception e) {
            return false;
        }

        return false;
    }

    private void antibanSetup() {
        Rs2AntibanSettings.antibanEnabled = true;
        Rs2AntibanSettings.usePlayStyle = true;
        Rs2AntibanSettings.randomIntervals = false;
        Rs2AntibanSettings.simulateFatigue = true;
        Rs2AntibanSettings.simulateAttentionSpan = true;
        Rs2AntibanSettings.behavioralVariability = true;
        Rs2AntibanSettings.nonLinearIntervals = true;
        Rs2AntibanSettings.profileSwitching = true;
        Rs2AntibanSettings.timeOfDayAdjust = false;
        Rs2AntibanSettings.simulateMistakes = false;
        Rs2AntibanSettings.naturalMouse = true;
        Rs2AntibanSettings.moveMouseOffScreen = true;
        Rs2AntibanSettings.moveMouseRandomly = true;
        Rs2AntibanSettings.contextualVariability = true;
        Rs2AntibanSettings.dynamicIntensity = false;
        Rs2AntibanSettings.dynamicActivity = false;
        Rs2AntibanSettings.devDebug = false;
        Rs2AntibanSettings.takeMicroBreaks = true;
        Rs2AntibanSettings.playSchedule = true;
        Rs2AntibanSettings.universalAntiban = false;
        Rs2AntibanSettings.moveMouseRandomlyChance = 0.05;
        Rs2AntibanSettings.microBreakDurationLow = 1;
        Rs2AntibanSettings.microBreakDurationHigh = 3;
        Rs2AntibanSettings.actionCooldownChance = 0.66;
        Rs2AntibanSettings.microBreakChance = 1.00;
        Rs2Antiban.setActivityIntensity(ActivityIntensity.MODERATE);
        Rs2Antiban.setPlayStyle(ActivityIntensity.MODERATE.getPlayStyle());
        Rs2Antiban.setCategory(Category.COMBAT_LOW);
    }

    private void flippingMain() {
        //Optional.ofNullable(Rs2Widget.getWidget(24772680)).ifPresent((x -> Rs2Widget.clickWidget(24772680)));

        Rs2GrandExchange.openExchange();
        sleepUntil(Rs2GrandExchange::isOpen);

        updateSuggestion();

        if(currentSuggestion.collectNeeded) {
            Rs2GrandExchange.collect(false);
            sleep(750, 1000);
        }

        switch(currentSuggestion.type) {
            case "buy":
                waitCount = 0;
                buyActionAlt();
                break;

            case "sell":
                waitCount = 0;
                sellAction();
                break;

            case "abort":
                waitCount = 0;
                abortAction();
                break;

            case "wait":
                waitAction();
                break;
        }
    }

    private void updateSuggestion() {
        if(currentSuggestion == null) currentSuggestion = new Suggestion();

        try {
            Optional<String> suggestionString = Optional.ofNullable((String) getCurrentSuggestion.invoke(null));

            if(suggestionString.isPresent()) {
                Suggestion newSuggestion = new Suggestion(suggestionString.get());

                if (!currentSuggestion.equals(newSuggestion)) {
                    currentSuggestion = newSuggestion;
                    System.out.println(currentSuggestion);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void buyActionAlt() {
        if (Rs2GrandExchange.getAvailableSlot().getRight() == 0) return;

        Optional<Boolean> canAfford = Optional.ofNullable(Rs2Inventory.get("coins", true))
                .map(coins -> coins.quantity >= currentSuggestion.price * currentSuggestion.quantity);

        if (!canAfford.orElse(false)) return;

        try {
            Pair<GrandExchangeSlots, Integer> slot = Rs2GrandExchange.getAvailableSlot();

            Optional<Widget> buyOffer = Optional.ofNullable(Rs2GrandExchange.getOfferBuyButton(slot.getLeft()));
            if (buyOffer.isEmpty()) return;

            Rs2Widget.clickWidgetFast(buyOffer.get());
            sleepUntil(Rs2GrandExchange::isOfferTextVisible, 5000);
            sleep(1000, 1500);

            //Click copilot recommended item
            sleepUntil(() -> Rs2Widget.clickChildWidget(10616882, 2), 2000);
            sleep(750, 1000);

            //Price using copilot menu item
            int currentPrice = Integer.parseInt(Rs2Widget.getChildWidgetText(30474265, 41)
                    .replaceAll("\\D", ""));

            if(currentPrice != currentSuggestion.price) {
                Optional<Widget> pricePerItemButtonX = Optional.ofNullable(Rs2GrandExchange.getPricePerItemButton_X());
                if(pricePerItemButtonX.isEmpty()) return;

                sleep(750, 1250);
                Microbot.getMouse().click(pricePerItemButtonX.get().getBounds());
                Microbot.getMouse().click(pricePerItemButtonX.get().getBounds());
                sleep(750, 1000);
                sleepUntil(() -> Rs2Widget.clickChildWidget(10616869, 0), 2000);
                sleep(500, 750);
                Rs2Keyboard.enter();
                sleep(750, 1000);
            }

            //Quantity using copilot menu item
            int currentQuantity = Integer.parseInt(Rs2Widget.getChildWidgetText(30474265, 34)
                    .replaceAll("\\D", ""));

            if(currentQuantity != currentSuggestion.quantity) {
                Optional<Widget> quantityButtonX = Optional.ofNullable(Rs2GrandExchange.getQuantityButton_X());
                if(quantityButtonX.isEmpty()) return;

                sleep(750, 1250);
                Microbot.getMouse().click(quantityButtonX.get().getBounds());
                Microbot.getMouse().click(quantityButtonX.get().getBounds());
                sleep(750, 1000);
                Rs2Widget.clickChildWidget(10616869, 0);
                sleep(500, 750);
                Rs2Keyboard.enter();
                sleep(750, 1000);
            }

            //Confirm
            Microbot.getMouse().click(Rs2GrandExchange.getConfirm().getBounds());
            sleepUntil(() -> Rs2Widget.hasWidget("Your offer is much higher"), 2000);
            if (Rs2Widget.hasWidget("Your offer is much higher")) {
                Rs2Widget.clickWidget("Yes");
            }
        } catch (Exception e) {
            System.out.println("Error during buy action");
            return;
        }
    }

    private void buyAction() {
        if(Rs2GrandExchange.getAvailableSlot().getRight() == 0) return;

        Optional<Boolean> canAfford = Optional.ofNullable(Rs2Inventory.get("coins", true))
                .map(coins -> coins.quantity >= currentSuggestion.price * currentSuggestion.quantity);

        if(!canAfford.orElse(false)) return;

        sleepUntil(() ->
                Rs2GrandExchange.buyItem(currentSuggestion.name, currentSuggestion.price, currentSuggestion.quantity),
                10000);

        Rs2Antiban.actionCooldown();
    }

    private void sellAction() {
        if(Rs2GrandExchange.getAvailableSlot().getRight() == 0) return;

        sleepUntil(() ->
                Rs2GrandExchange.sellItem(currentSuggestion.name, currentSuggestion.quantity, currentSuggestion.price),
                10000);

        Rs2Antiban.actionCooldown();
    }

    private void sellActionAlt() {
        if (Rs2GrandExchange.getAvailableSlot().getRight() == 0
                || !Rs2Inventory.hasItem(currentSuggestion.itemId)) return;

        try {
            Rs2Inventory.interact(currentSuggestion.itemId);
            sleep(300, 600);

            //Price using copilot menu item
            int currentPrice = Integer.parseInt(Rs2Widget.getChildWidgetText(30474265, 41)
                    .replaceAll("\\D", ""));

            if(currentPrice != currentSuggestion.price) {
                Optional<Widget> pricePerItemButtonX = Optional.ofNullable(Rs2GrandExchange.getPricePerItemButton_X());
                if(pricePerItemButtonX.isEmpty()) return;

                sleep(750, 1250);
                Microbot.getMouse().click(pricePerItemButtonX.get().getBounds());
                Microbot.getMouse().click(pricePerItemButtonX.get().getBounds());
                sleep(750, 1000);
                sleepUntil(() -> Rs2Widget.clickChildWidget(10616869, 0), 2000);
                sleep(500, 750);
                Rs2Keyboard.enter();
                sleep(750, 1000);
            }

            //Quantity using copilot menu item
            int currentQuantity = Integer.parseInt(Rs2Widget.getChildWidgetText(30474265, 34)
                    .replaceAll("\\D", ""));

            if(currentQuantity != currentSuggestion.quantity) {
                Optional<Widget> quantityButtonX = Optional.ofNullable(Rs2GrandExchange.getQuantityButton_X());
                if(quantityButtonX.isEmpty()) return;

                sleep(750, 1250);
                Microbot.getMouse().click(quantityButtonX.get().getBounds());
                Microbot.getMouse().click(quantityButtonX.get().getBounds());
                sleep(750, 1000);
                Rs2Widget.clickChildWidget(10616869, 0);
                sleep(500, 750);
                Rs2Keyboard.enter();
                sleep(750, 1000);
            }

            //Confirm
            Microbot.getMouse().click(Rs2GrandExchange.getConfirm().getBounds());
        } catch (NumberFormatException e) {
            System.out.println("Error during sell action");
            return;
        }
    }

    private void abortAction() {
        for(GrandExchangeSlots slot : GrandExchangeSlots.values()) {
            Optional<String> name = Optional.ofNullable(Rs2GrandExchange.getSlot(slot))
                    .map(x -> x.getChild(19))
                    .map(Widget::getText)
                    .filter(s -> s.equalsIgnoreCase(currentSuggestion.name));

            if(name.isPresent()) {
                Optional.ofNullable(Rs2GrandExchange.getSlot(slot))
                        .ifPresent(x -> Rs2Widget.clickWidgetFast(x, 2, 2));

                sleep(750, 1000);
                Rs2GrandExchange.collect(false);

                return;
            }
        }
    }

    private void waitAction() {
        sleep(5000);
        waitCount++;

        if(waitCount >= 5) {
            Rs2Antiban.takeMicroBreakByChance();
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @ToString
    @EqualsAndHashCode
    class Suggestion {
        private final String type;
        private final int itemId;
        private final int price;
        private final int quantity;
        private final String name;
        private final boolean collectNeeded;

        public Suggestion() {
            this.type = "wait";
            this.itemId = -1;
            this.price = -1;
            this.quantity = -1;
            this.name = "";
            this.collectNeeded = false;
        }

        public Suggestion(String input) {
            if(input != null) {
                Map<String, String> map = new HashMap<>();
                for (String s : input.replaceAll("^(.*?\\()|\\)$", "").split(",\\s")) {
                    String[] split = s.split("=");
                    map.put(split[0], (split.length >= 2) ? split[1] : "");
                }

                this.type = map.get("type");
                this.itemId = parseIntHandler(map.get("itemId"));
                this.price = parseIntHandler(map.get("price"));
                this.quantity = parseIntHandler(map.get("quantity"));
                this.name = map.get("name");
                this.collectNeeded = Boolean.parseBoolean(map.get("collectNeeded"));
            }
            else {
                this.type = "wait";
                this.itemId = -1;
                this.price = -1;
                this.quantity = -1;
                this.name = "";
                this.collectNeeded = false;
            }
        }

        private int parseIntHandler(String s) {
            try { return Integer.parseInt(s); }
            catch(NumberFormatException e) { return -1; }
        }
    }
}
