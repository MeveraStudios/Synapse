package studio.mevera.synapse.command;

import studio.mevera.imperat.BungeeImperat;
import studio.mevera.synapse.BungeePlugin;
import studio.mevera.synapse.command.impl.SynapseCommand;

public class CommandManager {

    private final BungeeImperat imperat;

    public CommandManager(final BungeePlugin plugin) {
        this.imperat = BungeeImperat.builder(plugin)
                .build();
        this.registerCommands();
    }

    private void registerCommands() {
        this.imperat.registerCommand(new SynapseCommand());
    }

}
