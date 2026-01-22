package studio.mevera.synapse;

import org.jetbrains.annotations.ApiStatus;
import studio.mevera.synapse.platform.BukkitNeuron;
import studio.mevera.synapse.platform.BukkitUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BukkitSynapse extends AdventureSynapseBase<CommandSender, BukkitUser, BukkitNeuron> {

    private static final BukkitSynapse INSTANCE = new BukkitSynapse();

    public static BukkitSynapse get() {
        return INSTANCE;
    }

    private final Map<CommandSender, BukkitUser> userCache = new ConcurrentHashMap<>();

    private BukkitSynapse() {
        // register default dependencies
        this.dependencyRegistry.register(BukkitSynapse.class, this);
    }

    @Override
    public BukkitUser asUser(final CommandSender origin) {
        if (origin instanceof CommandSender sender) {
            return this.userCache.computeIfAbsent(sender, BukkitUser::new);
        }
        throw new IllegalArgumentException("Origin must be an instance of CommandSender");
    }

    @ApiStatus.Internal
    void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.userCache.remove(player);
    }

}
