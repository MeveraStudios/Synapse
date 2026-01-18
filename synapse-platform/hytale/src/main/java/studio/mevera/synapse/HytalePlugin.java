package studio.mevera.synapse;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jetbrains.annotations.NotNull;
import studio.mevera.synapse.internal.HytaleInternalNeuron;
import studio.mevera.synapse.util.Utilities;

public final class HytalePlugin extends JavaPlugin {

    private static HytalePlugin instance;

    public HytalePlugin(@NotNull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void start() {
        getLogger().atInfo().log("\n" + Utilities.HYPHEN + "\n" + Utilities.ASCII_ART);
        instance = this;
        HytaleSynapse.get().registerNeuron(new HytaleInternalNeuron());
        getEventRegistry().register(PlayerDisconnectEvent.class, event -> HytaleSynapse.get().onQuit(event));
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
