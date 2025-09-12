package studio.mevera.synapse;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import studio.mevera.synapse.internal.VelocityInternalNeuron;
import studio.mevera.synapse.util.Utilities;

@Plugin(id = "synapse")
public final class VelocityPlugin {

    private static VelocityPlugin instance;

    private final ProxyServer server;
    private final PluginContainer container;
    private final Logger logger;

    @Inject
    public VelocityPlugin(final ProxyServer server, final Logger logger, final PluginContainer container) {
        this.server = server;
        this.logger = logger;
        this.container = container;
    }

    @Subscribe
    public void onInit(final ProxyInitializeEvent event) {
        logger.info(Utilities.HYPHEN);
        logger.info(Utilities.ASCII_ART);
        instance = this;
        this.server.getEventManager().register(this, this);
        VelocitySynapse.get().registerNeuron(new VelocityInternalNeuron());
        logger.info(Utilities.HYPHEN);
    }

    @Subscribe
    public void onShutdown(final ProxyShutdownEvent event) {
        instance = null;
    }

    @Subscribe
    public void onDisconnect(final DisconnectEvent event) {
        VelocitySynapse.get().onDisconnect(event);
    }

    public static VelocityPlugin getInstance() {
        return instance;
    }

    public ProxyServer getServer() {
        return server;
    }

    public PluginContainer getContainer() {
        return container;
    }

}
