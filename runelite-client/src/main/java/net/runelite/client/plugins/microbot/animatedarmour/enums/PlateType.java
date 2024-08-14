package net.runelite.client.plugins.microbot.animatedarmour.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlateType {
    BLACK("black platebody", "black platelegs", "black full helm"),
    MITHRIL("mithril platebody", "mithril platelegs", "mithril full helm"),
    ADAMANT("adamant platebody", "adamant platelegs", "adamant full helm"),
    RUNE("rune platebody", "rune platelegs", "rune full helm");

    private final String body;
    private final String legs;
    private final String helm;

    @Override
    public String toString() {
        final String ACTIONABLE_DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : name().toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0);
        }

        return sb.toString();
    }
}
