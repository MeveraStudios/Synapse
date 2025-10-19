package studio.mevera.synapse.platform;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.RelationalContext;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.type.ContextualPlaceholder;
import studio.mevera.synapse.placeholder.type.RelationalPlaceholder;
import studio.mevera.synapse.placeholder.type.StaticPlaceholder;

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
    void register(String name, String value, String... aliases);

    /**
     * Registers a static placeholder with a name and a supplier for the value.
     *
     * @param name    the name of the placeholder
     * @param value   the supplier for the value of the placeholder
     * @param aliases the aliases for the placeholder
     */
    void register(String name, Supplier<String> value, String... aliases);

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
            Supplier<String> supplier,
            Consumer<StaticPlaceholder.Options.Builder> options,
            String... aliases
    );

    /**
     * Registers a contextual placeholder with a name and a resolving function.
     *
     * @param name  the name of the placeholder
     * @param value the resolving function for the placeholder
     */
    void register(String name, Function<Context<U>, String> value, String... aliases);

    /**
     * Registers a contextual placeholder with a name, a resolving function, and options.
     *
     * @param name    the name of the placeholder
     * @param value   the resolving function for the placeholder
     * @param options the options for the placeholder
     */
    void register(
            String name,
            Function<Context<U>, String> value,
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
            Function<RelationalContext<U>, String> value,
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
            Function<RelationalContext<U>, String> value,
            Consumer<RelationalPlaceholder.Options.Builder> options,
            String... aliases
    );

    /**
     * Unregisters a placeholder with a specific name/tag.
     *
     * @param tag the tag of the placeholder to unregister
     */
    void unregister(String tag);
}
