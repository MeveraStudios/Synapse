package studio.mevera.synapse.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mevera.synapse.BukkitSynapse;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.BasicContex;
import studio.mevera.synapse.context.type.RelationalContext;
import studio.mevera.synapse.platform.BukkitNeuron;
import studio.mevera.synapse.platform.BukkitUser;

import java.util.Arrays;

public final class PAPIHook extends PlaceholderExpansion implements Relational {

    private final BukkitNeuron neuron;
    private final String namespace;

    public PAPIHook(final BukkitNeuron neuron, final String namespace) {
        this.neuron = neuron;
        this.namespace = namespace;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.namespace;
    }

    @Override
    public @NotNull String getAuthor() {
        return neuron.getPlugin().getName();
    }

    @Override
    public @NotNull String getVersion() {
        return neuron.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(
            final Player player,
            final @NotNull String params
    ) {
        if (player == null || params.isEmpty()) {
            return null;
        }

        try {
            final BukkitUser user = BukkitSynapse.get().asUser(player);

            // Fast path: check full string first (most common case)
            if (this.neuron.isRegistered(params)) {
                return this.neuron.onRequest(params, new BasicContex<>(
                        user,
                        params,
                        this.namespace
                ));
            }

            // Slow path: split and try progressively shorter names
            final String[] parts = params.split("_");
            if (parts.length == 1) {
                return null; // Already tried above
            }

            // Start from second-longest (we already tried full string)
            for (int i = parts.length - 1; i > 0; i--) {
                final String tag = String.join("_", Arrays.copyOfRange(parts, 0, i));

                if (this.neuron.isRegistered(tag)) {
                    final String[] args = Arrays.copyOfRange(parts, i, parts.length);

                    final Context<BukkitUser> context = new BasicContex<>(
                            user,
                            tag,
                            this.namespace,
                            args
                    );

                    return this.neuron.onRequest(tag, context);
                }
            }

            return null;
        } catch (Exception e) {
            this.neuron.getPlugin().getLogger().warning(
                    "Error processing PAPI placeholder '" + params + "': " + e.getMessage()
            );
            return null;
        }
    }

    @Override
    public @Nullable String onPlaceholderRequest(
            final Player one,
            final Player two,
            final @NotNull String params
    ) {
        if (one == null || two == null || params.isEmpty()) {
            return null;
        }

        try {
            final BukkitUser user = BukkitSynapse.get().asUser(one);
            final BukkitUser other = BukkitSynapse.get().asUser(two);

            // Fast path: check full string first (most common case)
            if (this.neuron.isRegistered(params)) {
                return this.neuron.onRequest(params, new RelationalContext<>(
                        user,
                        other,
                        params,
                        this.namespace
                ));
            }

            // Slow path: split and try progressively shorter names
            final String[] parts = params.split("_");
            if (parts.length == 1) {
                return null; // Already tried above
            }

            // Start from second-longest (we already tried full string)
            for (int i = parts.length - 1; i > 0; i--) {
                final String tag = String.join("_", Arrays.copyOfRange(parts, 0, i));

                if (this.neuron.isRegistered(tag)) {
                    final String[] args = Arrays.copyOfRange(parts, i, parts.length);

                    final Context<BukkitUser> context = new RelationalContext<>(
                            user,
                            other,
                            tag,
                            this.namespace,
                            args
                    );

                    return this.neuron.onRequest(tag, context);
                }
            }

            return null;
        } catch (Exception e) {
            this.neuron.getPlugin().getLogger().warning(
                    "Error processing PAPI relational placeholder '" + params + "': " + e.getMessage()
            );
            return null;
        }
    }

}
