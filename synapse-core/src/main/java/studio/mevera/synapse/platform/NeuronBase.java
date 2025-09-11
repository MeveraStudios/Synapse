package studio.mevera.synapse.platform;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.type.ContextualPlaceholder;
import studio.mevera.synapse.placeholder.type.RelationalPlaceholder;
import studio.mevera.synapse.placeholder.type.StaticPlaceholder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NeuronBase<U extends User> implements Neuron<U> {

    protected final Namespace namespace;
    protected final Map<String, Placeholder<U>> placeholders = new HashMap<>();

    public NeuronBase(final Namespace namespace) {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.namespace = namespace;
    }

    @Override
    public Namespace namespace() {
        return namespace;
    }

    @Override
    public String onRequest(final String tag, final Context<U> context) {
        final Placeholder<U> placeholder = this.placeholders.get(tag);
        if (placeholder == null) {
            return null;
        }
        return placeholder.resolve(context);
    }

    @Override
    public CompletableFuture<String> onRequestAsync(final String tag, final Context<U> context) {
        return CompletableFuture.supplyAsync(() -> this.onRequest(tag, context));
    }

    @Override
    public void register(final Placeholder<U> placeholder, final String... aliases) {
        this.placeholders.put(placeholder.name(), placeholder);
        for (final String alias : aliases) {
            this.placeholders.put(alias.toLowerCase(), placeholder);
        }
    }

    @Override
    public void register(
            final String name,
            final String value,
            final String... aliases
    ) {
        this.register(new StaticPlaceholder<>(name, value), aliases);
    }

    @Override
    public void register(
            final String name,
            final Supplier<String> value,
            final String... aliases
    ) {
        this.register(new StaticPlaceholder<>(name, value), aliases);
    }

    @Override
    public void register(
            final String name,
            final Supplier<String> supplier,
            final Consumer<StaticPlaceholder.Options.Builder> options,
            final String... aliases
    ) {
        this.register(new StaticPlaceholder<>(name, supplier, options), aliases);
    }

    @Override
    public void register(
            final String name,
            final ContextualPlaceholder.ResolvingFunction<U> value,
            final String... aliases
    ) {
        this.register(new ContextualPlaceholder<>(name, value), aliases);
    }

    @Override
    public void register(
            final String name,
            final ContextualPlaceholder.ResolvingFunction<U> value,
            final Consumer<ContextualPlaceholder.Options.Builder> options,
            final String... aliases
    ) {
        this.register(new ContextualPlaceholder<>(name, value, options), aliases);
    }

    @Override
    public void registerRelational(
            final String name,
            final RelationalPlaceholder.ResolvingFunction<U> value,
            final String... aliases
    ) {
        this.register(new RelationalPlaceholder<>(name, value), aliases);
    }

    @Override
    public void registerRelational(
            final String name,
            final RelationalPlaceholder.ResolvingFunction<U> value,
            final Consumer<RelationalPlaceholder.Options.Builder> options,
            final String... aliases
    ) {
        this.register(new RelationalPlaceholder<>(name, value, options), aliases);
    }

    @Override
    public boolean isRegistered(final String name) {
        return this.placeholders.containsKey(name);
    }

}
