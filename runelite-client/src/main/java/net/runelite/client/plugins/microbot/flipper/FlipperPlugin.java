package net.runelite.client.plugins.microbot.flipper;

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
        name = PluginDescriptor.Lalaz4 + "Flipper",
        description = "Microbot flipper plugin",
        tags = {"flipper", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class FlipperPlugin extends Plugin {
    @Inject
    private FlipperConfig config;
    @Provides
    FlipperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FlipperConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private FlipperOverlay flipperOverlay;

    @Inject
    FlipperScript flipperScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(flipperOverlay);
        }
        flipperScript.run(config);
    }

    protected void shutDown() {
        flipperScript.shutdown();
        overlayManager.remove(flipperOverlay);
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
