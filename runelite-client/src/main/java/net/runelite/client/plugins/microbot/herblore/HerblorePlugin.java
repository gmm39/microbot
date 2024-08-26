package net.runelite.client.plugins.microbot.herblore;

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
        name = PluginDescriptor.Lalaz4 + "AutoHerblore",
        description = "Microbot herblore plugin",
        tags = {"potion", "herblore", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class HerblorePlugin extends Plugin {
    @Inject
    private HerbloreConfig config;
    @Provides
    HerbloreConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(HerbloreConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private HerbloreOverlay HerbloreOverlay;

    @Inject
    HerbloreScript HerbloreScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(HerbloreOverlay);
        }
        HerbloreScript.run(config);
    }

    protected void shutDown() {
        HerbloreScript.shutdown();
        overlayManager.remove(HerbloreOverlay);
    }
    int ticks = 10;
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        if (ticks > 0) {
            ticks--;
        } else {
            ticks = 10;
        }

    }

}
