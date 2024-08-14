package net.runelite.client.plugins.microbot.animatedarmour;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.animatedarmour.enums.PlateType;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class AnimatedArmourOverlay extends OverlayPanel {
    @Inject
    AnimatedArmourOverlay(AnimatedArmourPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setNaughty();
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        try {
            panelComponent.setPreferredSize(new Dimension(200, 50));
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("[" + AnimatedArmourScript.version + "] Animated " +
                            AnimatedArmourScript.plate.toString() + " Armour")
                    .color(Color.GREEN)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("State: " + AnimatedArmourScript.state.name())
                    .build());

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return super.render(graphics);
    }
}
