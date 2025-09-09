package studio.mevera.synapse;

import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;

import java.util.concurrent.CompletableFuture;

/**
 * Synapse interface for translating text based on user context and registered neurons.
 *
 * @param <O> The type of origin (e.g., CommandSender).
 * @param <U> The type of user (e.g., BukkitUser).
 * @param <N> The type of neuron (e.g., BukkitNeuron).
 */
public interface Synapse<O, U extends User, N extends Neuron<U>> {

    /**
     * Translates the given text using the provided user context.
     *
     * @param text The text to translate, which may contain placeholders.
     * @param user The user context for translation, providing necessary information.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    String translate(String text, U user);

    /**
     * Translates the given text using the user created from the provided origin object.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param origin The origin object, typically a command sender or similar.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default String translate(String text, O origin) {
        return this.translate(text, this.asUser(origin));
    }

    /**
     * Translates the given text using the provided user context.
     *
     * @param text The text to translate, which may contain placeholders.
     * @param user The user context for translation, providing necessary information.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default CompletableFuture<String> translateAsync(String text, U user) {
        return CompletableFuture.supplyAsync(() -> this.translate(text, user));
    }

    /**
     * Translates the given text using the user created from the provided origin object.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param origin The origin object, typically a command sender or similar.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default CompletableFuture<String> translateAsync(String text, O origin) {
        return CompletableFuture.supplyAsync(() -> this.translate(text, this.asUser(origin)));
    }

    /**
     * Creates a user instance based on the provided origin object.
     *
     * @param origin The origin object, typically a command sender or similar.
     * @return A user instance corresponding to the provided origin.
     * @throws IllegalArgumentException if the origin is not of the expected type.
     */
    U asUser(O origin);

    /**
     * Registers a neuron with the Synapse instance.
     *
     * @param neuron The neuron to register.
     * @throws IllegalArgumentException if a neuron with the same namespace is already registered.
     */
    void registerNeuron(N neuron);

}
