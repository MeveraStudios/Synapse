package studio.mevera.synapse.platform;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.RelationalContext;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.type.ContextualPlaceholder;
import studio.mevera.synapse.placeholder.type.RelationalPlaceholder;
import studio.mevera.synapse.placeholder.type.StaticPlaceholder;
import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Neuron<U extends User> {

    /**
     * Returns the namespace of the neuron.
     *
     * @return the namespace of the neuron
     */
    Namespace namespace();

    /**
     * Handles a request for a placeholder.
     *
     * @param tag the tag of the placeholder
     * @param context the context of the request
     */
    String onRequest(String tag, Context<U> context);

    /**
     * Handles a request for a placeholder asynchronously.
     *
     * @param tag the tag of the placeholder
     * @param context the context of the request
     * @return a CompletableFuture that will complete with the resolved value
     */
    CompletableFuture<String> onRequestAsync(String tag, Context<U> context);

    /**
     * Checks if a placeholder with a specific name/tag is registered
     *
     * @param tag the tag requested
     */
    boolean isRegistered(String tag);

    /**
     * Registers the neuron itself onto Synapse.
     */
    void register();

    /**
     * Registers a placeholder with this neuron.
     *
     * @param placeholder the placeholder to register
     * @param aliases     the aliases for the placeholder
     */
    void register(Placeholder<U> placeholder, String... aliases);

    /**
     * Registers a static placeholder with a name and a value.
     *
     * @param name    the name of the placeholder
     * @param value   the value of the placeholder
     * @param aliases the aliases for the placeholder
     */
    void register(String name, Object value, String... aliases);

    /**
     * Registers a static placeholder with a name and a supplier for the value.
     *
     * @param name    the name of the placeholder
     * @param value   the supplier for the value of the placeholder
     * @param aliases the aliases for the placeholder
     */
    void register(String name, Supplier<Object> value, String... aliases);

    /**
     * Registers a static placeholder with a name, a supplier for the value, and options.
     *
     * @param name     the name of the placeholder
     * @param supplier the supplier for the value of the placeholder
     * @param options  the options for the placeholder
     * @param aliases  the aliases for the placeholder
     */
    void register(
            String name,
            Supplier<Object> supplier,
            Consumer<StaticPlaceholder.Options.Builder> options,
            String... aliases
    );

    /**
     * Registers a contextual placeholder with a name and a resolving function.
     *
     * @param name  the name of the placeholder
     * @param value the resolving function for the placeholder
     */
    void register(String name, Function<Context<U>, Object> value, String... aliases);

    /**
     * Registers a contextual placeholder with a name, a resolving function, and options.
     *
     * @param name    the name of the placeholder
     * @param value   the resolving function for the placeholder
     * @param options the options for the placeholder
     */
    void register(
            String name,
            Function<Context<U>, Object> value,
            Consumer<ContextualPlaceholder.Options.Builder> options,
            String... aliases
    );

    /**
     * Registers a relational placeholder with a name and a resolving function.
     *
     * @param name  the name of the placeholder
     * @param value the resolving function for the placeholder
     */
    void registerRelational(
            String name,
            Function<RelationalContext<U>, Object> value,
            String... aliases
    );

    /**
     * Registers a relational placeholder with a name, a resolving function, and options.
     *
     * @param name    the name of the placeholder
     * @param value   the resolving function for the placeholder
     * @param options the options for the placeholder
     */
    void registerRelational(
            String name,
            Function<RelationalContext<U>, Object> value,
            Consumer<RelationalPlaceholder.Options.Builder> options,
            String... aliases
    );

    /**
     * Unregisters a placeholder with a specific name/tag.
     *
     * @param tag the tag of the placeholder to unregister
     */
    void unregister(String tag);

    /**
     * Handles a specific type using the registered type handlers.
     *
     * @param value The value to handle.
     * @return The string representation of the value, or null if no handler is found.
     */
    String handleType(Object value);

    /**
     * Registers a type handler for a specific type.
     *
     * @param handler The type handler to register.
     * @param <T>     The type the handler handles.
     */
    <T> void registerTypeHandler(TypeHandler<T> handler);

    /**
     * Registers a type handler function for a specific type.
     *
     * @param type    The type the handler function handles.
     * @param handler The handler function to register.
     * @param <T>     The type the handler function handles.
     */
    <T> void registerTypeHandler(Type type, TypeHandlerRegistry.HandlerFunction<T> handler);

    /**
     * Unregisters a type handler for a specific type.
     *
     * @param type The type whose handler should be unregistered.
     */
    void unregisterTypeHandler(Type type);

}
