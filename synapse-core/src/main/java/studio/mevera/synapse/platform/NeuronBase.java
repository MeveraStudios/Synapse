package studio.mevera.synapse.platform;

import org.jetbrains.annotations.ApiStatus;
import studio.mevera.synapse.annotation.NeuronEntry;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.RelationalContext;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.type.ContextualPlaceholder;
import studio.mevera.synapse.placeholder.type.RelationalPlaceholder;
import studio.mevera.synapse.placeholder.type.StaticPlaceholder;
import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;
import studio.mevera.synapse.type.impl.*;
import studio.mevera.synapse.type.impl.date.DurationHandler;
import studio.mevera.synapse.type.impl.date.InstantHandler;
import studio.mevera.synapse.type.impl.date.LocalDateTimeHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class NeuronBase<U extends User> implements Neuron<U> {

    protected final Namespace namespace;
    protected final Map<String, Placeholder<U>> placeholders = new HashMap<>();
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    protected final boolean internallyLoaded = this.getClass().isAnnotationPresent(NeuronEntry.class);

    public NeuronBase(final Namespace namespace) {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        this.namespace = namespace;
        this.initializeDefaultTypeHandlers();
    }

    protected void initializeDefaultTypeHandlers() {
        // primitive type handlers
        this.registerTypeHandler(new EnumHandler());
        this.registerTypeHandler(new StringHandler());
        this.registerTypeHandler(new NumberHandler());
        this.registerTypeHandler(new BooleanHandler());

        // date/time type handlers
        this.registerTypeHandler(new DurationHandler());
        this.registerTypeHandler(new InstantHandler());
        this.registerTypeHandler(new LocalDateTimeHandler());

        // collection type handlers
        this.registerTypeHandler(new MapHandler(typeHandlerRegistry));
        this.registerTypeHandler(new ArrayHandler(typeHandlerRegistry));
        this.registerTypeHandler(new CollectionHandler(typeHandlerRegistry));

        // wrapper type handlers
        this.registerTypeHandler(new SupplierHandler(typeHandlerRegistry));
        this.registerTypeHandler(new OptionalHandler(typeHandlerRegistry));
        this.registerTypeHandler(new CompletableFutureHandler(typeHandlerRegistry));
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
        return this.handleType(placeholder.resolve(context));
    }

    @Override
    public CompletableFuture<String> onRequestAsync(final String tag, final Context<U> context) {
        return CompletableFuture.supplyAsync(() -> this.onRequest(tag, context));
    }

    @Override
    public void register(final Placeholder<U> placeholder, final String... aliases) {
        Objects.requireNonNull(placeholder, "Placeholder cannot be null");
        Objects.requireNonNull(placeholder.name(), "Placeholder name cannot be null");
        this.placeholders.put(placeholder.name().toLowerCase(), placeholder);
        for (final String alias : aliases) {
            if (alias == null || alias.isEmpty()) continue;
            this.placeholders.put(alias.toLowerCase(), placeholder);
        }
    }

    @Override
    public void register(
            final String name,
            final Object value,
            final String... aliases
    ) {
        this.register(new StaticPlaceholder<>(name, value), aliases);
    }

    @Override
    public void register(
            final String name,
            final Supplier<Object> value,
            final String... aliases
    ) {
        this.register(new StaticPlaceholder<>(name, value), aliases);
    }

    @Override
    public void register(
            final String name,
            final Supplier<Object> supplier,
            final Consumer<StaticPlaceholder.Options.Builder> options,
            final String... aliases
    ) {
        this.register(new StaticPlaceholder<>(name, supplier, options), aliases);
    }

    @Override
    public void register(
            final String name,
            final Function<Context<U>, Object> value,
            final String... aliases
    ) {
        this.register(new ContextualPlaceholder<>(name, value), aliases);
    }

    @Override
    public void register(
            final String name,
            final Function<Context<U>, Object> value,
            final Consumer<ContextualPlaceholder.Options.Builder> options,
            final String... aliases
    ) {
        this.register(new ContextualPlaceholder<>(name, value, options), aliases);
    }

    @Override
    public void registerRelational(
            final String name,
            final Function<RelationalContext<U>, Object> value,
            final String... aliases
    ) {
        this.register(new RelationalPlaceholder<>(name, value), aliases);
    }

    @Override
    public void registerRelational(
            final String name,
            final Function<RelationalContext<U>, Object> value,
            final Consumer<RelationalPlaceholder.Options.Builder> options,
            final String... aliases
    ) {
        this.register(new RelationalPlaceholder<>(name, value, options), aliases);
    }

    @Override
    public boolean isRegistered(final String name) {
        return name != null && this.placeholders.containsKey(name.toLowerCase());
    }

    @Override
    public void unregister(final String tag) {
        if (tag == null) return;
        final Placeholder<U> placeholder = this.placeholders.remove(tag.toLowerCase());
        if (placeholder == null) return;
        this.placeholders.values().removeIf(p -> p == placeholder);
    }

    @Override
    public String handleType(Object value) {
        return this.typeHandlerRegistry.handle(value);
    }

    @Override
    public <T> void registerTypeHandler(final TypeHandler<T> handler) {
        this.typeHandlerRegistry.register(handler);
    }

    @Override
    public <T> void registerTypeHandler(final Type type, final TypeHandlerRegistry.HandlerFunction<T> handler) {
        this.typeHandlerRegistry.register(type, handler);
    }

    @Override
    public void unregisterTypeHandler(final Type type) {
        this.typeHandlerRegistry.unregister(type);
    }

    @ApiStatus.Internal
    public boolean isInternallyLoaded() {
        return internallyLoaded;
    }
}
