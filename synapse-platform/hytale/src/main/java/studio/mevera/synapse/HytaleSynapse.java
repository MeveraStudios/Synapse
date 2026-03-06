package studio.mevera.synapse;

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.Universe;
import org.jetbrains.annotations.ApiStatus;
import studio.mevera.synapse.platform.HytaleNeuron;
import studio.mevera.synapse.platform.HytaleUser;
import studio.mevera.synapse.platform.Platform;
import studio.mevera.synapse.platform.PlatformInfo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class HytaleSynapse extends SynapseBase<CommandSender, HytaleUser, HytaleNeuron> {

    private static final HytaleSynapse INSTANCE = new HytaleSynapse();
    private static final Platform platform = new PlatformInfo("hytale", Universe.MANIFEST.getServerVersion());

    public static HytaleSynapse get() {
        return INSTANCE;
    }

    private final Map<UUID, HytaleUser> userCache = new ConcurrentHashMap<>();

    private HytaleSynapse() {
        // register default dependencies
        this.dependencyRegistry.register(HytaleSynapse.class, this);
    }

    @Override
    public Platform platform() {
        return platform;
    }

    @Override
    public HytaleUser asUser(final CommandSender origin) {
        if (origin instanceof CommandSender sender) {
            return this.userCache.computeIfAbsent(sender.getUuid(), uuid -> new HytaleUser(sender));
        }
        throw new IllegalArgumentException("Origin must be an instance of CommandSender");
    }

    @ApiStatus.Internal
    void onQuit(final PlayerDisconnectEvent event) {
        this.userCache.remove(event.getPlayerRef().getUuid());
    }

}
