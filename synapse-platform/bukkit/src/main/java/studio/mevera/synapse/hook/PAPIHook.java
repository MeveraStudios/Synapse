package studio.mevera.synapse.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mevera.synapse.BukkitSynapse;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.ContextBase;
import studio.mevera.synapse.platform.BukkitNeuron;
import studio.mevera.synapse.platform.BukkitUser;

import java.util.Arrays;
import java.util.NoSuchElementException;

public final class PAPIHook extends PlaceholderExpansion {

    private final BukkitNeuron neuron;
    private final String namespace;

    public PAPIHook(final BukkitNeuron neuron) {
        this.neuron = neuron;
        this.namespace = neuron.namespace().firstName().orElseThrow(() -> new NoSuchElementException("There was no namespace found for a registered neuron"));
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
            return "";
        }

        final BukkitUser user = BukkitSynapse.get().asUser(player);
        final String[] splitParams = params.split("_");
        final String tag = splitParams[0];

        if (!this.neuron.isRegistered(tag)) {
            return null;
        }

        final Context<BukkitUser> context = new ContextBase<>(
                user,
                tag,
                this.namespace,
                Arrays.stream(splitParams).skip(1).toArray(String[]::new)
        );

        return this.neuron.onRequest(tag, context);
    }

}
