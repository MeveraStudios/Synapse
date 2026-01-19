package studio.mevera.synapse.command;

import studio.mevera.imperat.BukkitImperat;
import studio.mevera.synapse.BukkitPlugin;
import studio.mevera.synapse.command.impl.SynapseCommand;

public class CommandManager {

    private final BukkitImperat imperat;

    public CommandManager(final BukkitPlugin plugin) {
        this.imperat = BukkitImperat.builder(plugin)
                .build();
        this.registerCommands();
    }

    private void registerCommands() {
        this.imperat.registerCommand(new SynapseCommand());
    }

}
