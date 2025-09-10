package studio.mevera.synapse;

import lombok.Getter;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import studio.mevera.synapse.internal.BungeeInternalNeuron;

public final class BungeePlugin extends Plugin implements Listener {

    @Getter
    private static BungeePlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        getProxy().getPluginManager().registerListener(this, this);
        BungeeSynapse.get().registerNeuron(new BungeeInternalNeuron());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void onDisconnect(final PlayerDisconnectEvent event) {
        BungeeSynapse.get().onDisconnect(event);
    }
}
