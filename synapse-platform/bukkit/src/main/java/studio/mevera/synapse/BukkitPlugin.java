package studio.mevera.synapse;

import studio.mevera.synapse.internal.BukkitInternalNeuron;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import studio.mevera.synapse.util.LibraryLoader;
import studio.mevera.synapse.util.Utilities;

public final class BukkitPlugin extends JavaPlugin implements Listener {

    private static BukkitPlugin instance;

    @Override
    public void onEnable() {
        getLogger().info("\n" + Utilities.HYPHEN + "\n" + Utilities.ASCII_ART);
        instance = this;
        LibraryLoader.loadLibraries();
        BukkitSynapse.get().registerNeuron(new BukkitInternalNeuron());
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("\n" + Utilities.HYPHEN);
    }

    public static BukkitPlugin getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        BukkitSynapse.get().onQuit(event);
    }

}
