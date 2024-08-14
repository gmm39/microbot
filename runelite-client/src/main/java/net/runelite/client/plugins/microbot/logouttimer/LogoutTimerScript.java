package net.runelite.client.plugins.microbot.logouttimer;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


public class LogoutTimerScript extends Script {
    public static double version = 1.0;
    public static int logoutTime;

    public static Duration duration;

    public boolean run(LogoutTimerConfig config) {
        Microbot.enableAutoRunOn = false;
        logoutTime = config.time() * 60;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

                if(logoutTime > 0) {
                    logoutTime--;
                    duration = Duration.between(LocalDateTime.now(),LocalDateTime.now().plusSeconds(logoutTime));
                }

                else {
                    Microbot.pauseAllScripts = true;
                    Rs2Player.logout();
                }


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
