package studio.mevera.synapse;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import org.jetbrains.annotations.ApiStatus;
import studio.mevera.synapse.platform.BungeeNeuron;
import studio.mevera.synapse.platform.BungeeUser;
import studio.mevera.synapse.platform.Platform;
import studio.mevera.synapse.platform.PlatformInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BungeeSynapse extends AdventureSynapseBase<CommandSender, BungeeUser, BungeeNeuron> {

    private static final BungeeSynapse INSTANCE = new BungeeSynapse();
    private static final Platform platform = new PlatformInfo("bungeecord", net.md_5.bungee.api.ProxyServer.getInstance().getVersion());

    public static BungeeSynapse get() {
        return INSTANCE;
    }

    private final Map<CommandSender, BungeeUser> userCache = new ConcurrentHashMap<>();

    private BungeeSynapse() {
        // register default dependencies
        this.dependencyRegistry.register(BungeeSynapse.class, this);
    }

    @Override
    public Platform platform() {
        return platform;
    }

    @Override
    public BungeeUser asUser(final CommandSender origin) {
        if (origin instanceof CommandSender sender) {
            return this.userCache.computeIfAbsent(sender, BungeeUser::new);
        }
        throw new IllegalArgumentException("Origin must be an instance of CommandSender");
    }

    @ApiStatus.Internal
    void onDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        this.userCache.remove(player);
    }
}
