package studio.mevera.synapse;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import studio.mevera.synapse.command.CommandManager;
import studio.mevera.synapse.internal.BungeeInternalNeuron;
import studio.mevera.synapse.util.LibraryLoader;
import studio.mevera.synapse.util.Utilities;

public final class BungeePlugin extends Plugin implements Listener {

    private static BungeePlugin instance;

    @Override
    public void onEnable() {
        getLogger().info("\n" + Utilities.HYPHEN + "\n" + Utilities.ASCII_ART);
        instance = this;
        LibraryLoader.loadLibraries();
        getProxy().getPluginManager().registerListener(this, this);
        BungeeSynapse.get().registerNeuron(new BungeeInternalNeuron());
        new CommandManager(this);
        getLogger().info("\n" + Utilities.HYPHEN);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void onDisconnect(final PlayerDisconnectEvent event) {
        BungeeSynapse.get().onDisconnect(event);
    }

    public static BungeePlugin getInstance() {
        return instance;
    }
}
