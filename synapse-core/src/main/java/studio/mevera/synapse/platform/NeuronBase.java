package studio.mevera.synapse.platform;

import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.type.ContextualPlaceholder;
import studio.mevera.synapse.placeholder.type.StaticPlaceholder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NeuronBase<U extends User> implements Neuron<U> {

    private final Namespace namespace;
    private final Map<String, Placeholder<U>> placeholders = new HashMap<>();

    public NeuronBase(final Namespace namespace) {
        this.namespace = namespace;
        if (namespace == null) {
            throw new IllegalArgumentException("Namespace cannot be null");
        }
    }

    @Override
    public Namespace namespace() {
        return namespace;
    }

    @Override
    public void register(final Placeholder<U> placeholder) {
        this.placeholders.put(placeholder.name(), placeholder);
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

    /**
     * Registers a static placeholder with a name and a value.
     *
     * @param name  the name of the placeholder
     * @param value the value of the placeholder
     */
    @Override
    public void register(
            final String name,
            final String value
    ) {
        this.register(new StaticPlaceholder<>(name, value));
    }

    /**
     * Registers a static placeholder with a name and a supplier for the value.
     *
     * @param name  the name of the placeholder
     * @param value the supplier for the value of the placeholder
     */
    @Override
    public void register(
            final String name,
            final Supplier<String> value
    ) {
        this.register(new StaticPlaceholder<>(name, value));
    }

    /**
     * Registers a static placeholder with a name, a supplier for the value, and options.
     *
     * @param name     the name of the placeholder
     * @param supplier the supplier for the value of the placeholder
     * @param options  the options for the placeholder
     */
    @Override
    public void register(
            final String name,
            final Supplier<String> supplier,
            final Consumer<StaticPlaceholder.Options.Builder> options
    ) {
        this.register(new StaticPlaceholder<>(name, supplier, options));
    }

    /**
     * Registers a contextual placeholder with a name and a resolving function.
     *
     * @param name  the name of the placeholder
     * @param value the resolving function for the placeholder
     */
    @Override
    public void register(
            final String name,
            final ContextualPlaceholder.ResolvingFunction<U> value
    ) {
        this.register(new ContextualPlaceholder<>(name, value));
    }

    /**
     * Registers a contextual placeholder with a name, a resolving function, and options.
     *
     * @param name    the name of the placeholder
     * @param value   the resolving function for the placeholder
     * @param options the options for the placeholder
     */
    @Override
    public void register(
            final String name,
            final ContextualPlaceholder.ResolvingFunction<U> value,
            final Consumer<ContextualPlaceholder.Options.Builder> options
    ) {
        this.register(new ContextualPlaceholder<>(name, value, options));
    }

}
