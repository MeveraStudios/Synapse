package studio.mevera.synapse;

import studio.mevera.synapse.internal.BukkitInternalNeuron;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlugin extends JavaPlugin implements Listener {

    @Getter
    private static BukkitPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        BukkitSynapse.get().registerNeuron(new BukkitInternalNeuron());
        getServer().getPluginManager().registerEvents(this, this);
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
