package studio.mevera.synapse;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jetbrains.annotations.NotNull;
import studio.mevera.synapse.command.CommandManager;
import studio.mevera.synapse.internal.HytaleInternalNeuron;
import studio.mevera.synapse.util.Utilities;

public final class HytalePlugin extends JavaPlugin {

    private static HytalePlugin instance;

    public HytalePlugin(@NotNull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        getLogger().atInfo().log("\n" + Utilities.HYPHEN + "\n" + Utilities.ASCII_ART);
        instance = this;
        getEventRegistry().register(PlayerDisconnectEvent.class, event -> HytaleSynapse.get().onQuit(event));
        HytaleSynapse.get().registerNeuron(new HytaleInternalNeuron());
        new CommandManager(this);
        getLogger().atInfo().log("\n" + Utilities.HYPHEN);
    }

    @Override
    protected void shutdown() {
        instance = null;
    }

    public static HytalePlugin getInstance() {
        return instance;
    }

}
