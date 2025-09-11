package studio.mevera.synapse.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mevera.synapse.BukkitSynapse;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.BasicContex;
import studio.mevera.synapse.context.type.RelationalContex;
import studio.mevera.synapse.platform.BukkitNeuron;
import studio.mevera.synapse.platform.BukkitUser;

import java.util.Arrays;

public final class PAPIHook extends PlaceholderExpansion implements Relational {

    private final BukkitNeuron neuron;
    private final String namespace;

    public PAPIHook(final BukkitNeuron neuron) {
        this.neuron = neuron;
        this.namespace = neuron.namespace().firstName().orElseThrow(() -> new IllegalArgumentException("There was no namespace found for a registered neuron"));
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
        if (player == null) {
            return null;
        }

        final BukkitUser user = BukkitSynapse.get().asUser(player);
        final String[] splitParams = params.split("_");
        final String tag = splitParams[0];

        if (!this.neuron.isRegistered(tag)) {
            return null;
        }

        final Context<BukkitUser> context = new BasicContex<>(
                user,
                tag,
                this.namespace,
                this.skipOne(splitParams)
        );

        return this.neuron.onRequest(tag, context);
    }

    @Override
    public String onPlaceholderRequest(final Player one, final Player two, final String params) {
        if (one == null || two == null) {
            return null;
        }

        final BukkitUser user = BukkitSynapse.get().asUser(one);
        final BukkitUser other = BukkitSynapse.get().asUser(two);

        final String[] splitParams = params.split("_");
        final String tag = splitParams[0];

        if (!this.neuron.isRegistered(tag)) {
            return null;
        }

        final Context<BukkitUser> context = new RelationalContex<>(
                user,
                other,
                tag,
                this.namespace,
                this.skipOne(splitParams)
        );

        return this.neuron.onRequest(tag, context);
    }

    private String[] skipOne(final String[] array) {
        if (array.length <= 1) {
            return new String[0];
        }
        return Arrays.copyOfRange(array, 1, array.length);
    }

}
