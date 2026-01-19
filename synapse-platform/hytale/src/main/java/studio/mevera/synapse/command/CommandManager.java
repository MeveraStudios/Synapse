package studio.mevera.synapse.command;

import studio.mevera.imperat.HytaleImperat;
import studio.mevera.synapse.HytalePlugin;
import studio.mevera.synapse.command.impl.SynapseCommand;

public class CommandManager {

    private final HytaleImperat imperat;

    public CommandManager(final HytalePlugin plugin) {
        this.imperat = HytaleImperat
                .builder(plugin)
                .build();
        this.registerCommands();
    }

    private void registerCommands() {
        this.imperat.registerCommand(new SynapseCommand());
    }

}
