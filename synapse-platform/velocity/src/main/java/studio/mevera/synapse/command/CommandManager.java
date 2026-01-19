package studio.mevera.synapse.command;

import studio.mevera.imperat.VelocityImperat;
import studio.mevera.synapse.VelocityPlugin;
import studio.mevera.synapse.command.impl.SynapseCommand;

public class CommandManager {

    private final VelocityImperat<VelocityPlugin> imperat;

    public CommandManager(final VelocityPlugin plugin) {
        this.imperat = VelocityImperat
                .builder(plugin, plugin.getServer())
                .build();
        this.registerCommands();
    }

    private void registerCommands() {
        this.imperat.registerCommand(new SynapseCommand());
    }

}
