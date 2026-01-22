package studio.mevera.synapse;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.ApiStatus;
import studio.mevera.synapse.platform.VelocityNeuron;
import studio.mevera.synapse.platform.VelocityUser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class VelocitySynapse extends AdventureSynapseBase<CommandSource, VelocityUser, VelocityNeuron> {

    private static final VelocitySynapse INSTANCE = new VelocitySynapse();

    public static VelocitySynapse get() {
        return INSTANCE;
    }

    private final Map<CommandSource, VelocityUser> userCache = new ConcurrentHashMap<>();

    private VelocitySynapse() {
        // register default dependencies
        this.dependencyRegistry.register(VelocitySynapse.class, this);
    }

    @Override
    public VelocityUser asUser(final CommandSource origin) {
        if (origin instanceof CommandSource source) {
            return this.userCache.computeIfAbsent(source, VelocityUser::new);
        }
        throw new IllegalArgumentException("Origin must be an instance of CommandSource");
    }

    @ApiStatus.Internal
    void onDisconnect(final DisconnectEvent event) {
        final Player player = event.getPlayer();
        this.userCache.remove(player);
    }
}
