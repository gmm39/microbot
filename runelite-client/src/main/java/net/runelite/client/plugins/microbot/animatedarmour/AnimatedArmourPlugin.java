package net.runelite.client.plugins.microbot.animatedarmour;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Lalaz4 + "AnimatedArmour",
        description = "Microbot animated armour plugin",
        tags = {"animated", "armour", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class AnimatedArmourPlugin extends Plugin {
    @Inject
    private AnimatedArmourConfig config;
    @Provides
    AnimatedArmourConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AnimatedArmourConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private AnimatedArmourOverlay AnimatedArmourOverlay;

    @Inject
    AnimatedArmourScript AnimatedArmourScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(AnimatedArmourOverlay);
        }
        AnimatedArmourScript.run(config);
    }

    protected void shutDown() {
        AnimatedArmourScript.shutdown();
        overlayManager.remove(AnimatedArmourOverlay);
    }
    int ticks = 10;
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        //System.out.println(getName().chars().mapToObj(i -> (char)(i + 3)).map(String::valueOf).collect(Collectors.joining()));

        if (ticks > 0) {
            ticks--;
        } else {
            ticks = 10;
        }

    }

}
