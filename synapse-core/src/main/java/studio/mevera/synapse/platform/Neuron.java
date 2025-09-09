package studio.mevera.synapse.platform;

import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.type.ContextualPlaceholder;
import studio.mevera.synapse.placeholder.type.StaticPlaceholder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Neuron<U extends User> {

    /**
     * Returns the namespace of the neuron.
     *
     * @return the namespace of the neuron
     */
    Namespace namespace();

    /**
     * Registers a placeholder with this neuron.
     *
     * @param placeholder the placeholder to register
     */
    void register(Placeholder<U> placeholder);

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
     * Registers a static placeholder with a name and a value.
     *
     * @param name  the name of the placeholder
     * @param value the value of the placeholder
     */
    void register(String name, String value);

    /**
     * Registers a static placeholder with a name and a supplier for the value.
     *
     * @param name  the name of the placeholder
     * @param value the supplier for the value of the placeholder
     */
    void register(String name, Supplier<String> value);

    /**
     * Registers a static placeholder with a name, a supplier for the value, and options.
     *
     * @param name     the name of the placeholder
     * @param supplier the supplier for the value of the placeholder
     * @param options  the options for the placeholder
     */
    void register(
            String name,
            Supplier<String> supplier,
            Consumer<StaticPlaceholder.Options.Builder> options
    );

    /**
     * Registers a contextual placeholder with a name and a resolving function.
     *
     * @param name  the name of the placeholder
     * @param value the resolving function for the placeholder
     */
    void register(String name, ContextualPlaceholder.ResolvingFunction<U> value);

    /**
     * Registers a contextual placeholder with a name, a resolving function, and options.
     *
     * @param name    the name of the placeholder
     * @param value   the resolving function for the placeholder
     * @param options the options for the placeholder
     */
    void register(
            String name,
            ContextualPlaceholder.ResolvingFunction<U> value,
            Consumer<ContextualPlaceholder.Options.Builder> options
    );

}
