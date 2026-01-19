package studio.mevera.synapse.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SynapseHelpMessage {

    public static Component getHelpMessage() {
        return Component.text("Synapse")
                .color(NamedTextColor.AQUA)
                .decorate(TextDecoration.BOLD)
                .append(Component.text(" - Commands\n")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.BOLD, false))
                .append(Component.newline())
                .append(Component.text("/synapse help")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text(" - Show this help message\n")
                                .color(NamedTextColor.GRAY)))
                .append(Component.text("/synapse selfparse ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text("<text>")
                                .color(NamedTextColor.YELLOW))
                        .append(Component.text(" - Parse text as yourself\n")
                                .color(NamedTextColor.GRAY)))
                .append(Component.text("/synapse parse ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text("<player> ")
                                .color(NamedTextColor.YELLOW))
                        .append(Component.text("<text>")
                                .color(NamedTextColor.YELLOW))
                        .append(Component.text(" - Parse text as another player")
                                .color(NamedTextColor.GRAY)));
    }

}