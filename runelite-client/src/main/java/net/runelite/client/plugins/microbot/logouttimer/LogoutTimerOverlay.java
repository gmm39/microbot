package net.runelite.client.plugins.microbot.logouttimer;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class LogoutTimerOverlay extends OverlayPanel {
    @Inject
    LogoutTimerOverlay(LogoutTimerPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setNaughty();
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        try {
            panelComponent.setPreferredSize(new Dimension(150, 50));
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("[" + net.runelite.client.plugins.microbot.logouttimer.LogoutTimerScript.version + "] LogoutTimer")
                    .color(Color.GREEN)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder().build());

            long hours = LogoutTimerScript.duration.toHours();
            long minutes = LogoutTimerScript.duration.toMinutes() % 60;
            long seconds = LogoutTimerScript.duration.getSeconds() % 60;

            if (net.runelite.client.plugins.microbot.logouttimer.LogoutTimerScript.logoutTime > 0) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left(String.format("Logout in: %02d:%02d:%02d%n", hours, minutes, seconds))
                        .build());
            }

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return super.render(graphics);
    }
}
