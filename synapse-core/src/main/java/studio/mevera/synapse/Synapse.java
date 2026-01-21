package studio.mevera.synapse;

import org.jetbrains.annotations.NotNull;
import studio.mevera.synapse.log.SynapseLogger;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
     * Translates the given text using the provided user context and another user.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param user   The primary user context for translation.
     * @param other  The secondary user context for translation.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    String translate(String text, U user, U other);

    /**
     * Translates the given text using the users created from the provided origin objects.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param origin The origin object of the primary user.
     * @param other The origin object of the other user.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default String translate(String text, O origin, O other) {
        return this.translate(text, this.asUser(origin), this.asUser(other));
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
     * Translates the given text using the provided user context.
     *
     * @param text The text to translate, which may contain placeholders.
     * @param user The user context for translation, providing necessary information.
     * @param executor The executor to run the translation task.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default CompletableFuture<String> translateAsync(String text, U user, Executor executor) {
        return CompletableFuture.supplyAsync(() -> this.translate(text, user), executor);
    }

    /**
     * Translates the given text using the provided user context and another user asynchronously.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param user   The primary user context.
     * @param other  The other user context.
     * @return A CompletableFuture that will complete with the translated text.
     */
    default CompletableFuture<String> translateAsync(String text, U user, U other) {
        return CompletableFuture.supplyAsync(() -> this.translate(text, user, other));
    }

    /**
     * Translates the given text using the provided user context and another user asynchronously.
     *
     * @param text     The text to translate, which may contain placeholders.
     * @param user     The primary user context.
     * @param other    The other user context.
     * @param executor The executor to run the translation task.
     * @return A CompletableFuture that will complete with the translated text.
     */
    default CompletableFuture<String> translateAsync(String text, U user, U other, Executor executor) {
        return CompletableFuture.supplyAsync(() -> this.translate(text, user, other), executor);
    }

    /**
     * Translates the given text using the user created from the provided origin object.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param origin The origin object, typically a command sender or similar.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default CompletableFuture<String> translateAsync(String text, O origin) {
        return this.translateAsync(text, this.asUser(origin));
    }

    /**
     * Translates the given text using the user created from the provided origin object.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param origin The origin object, typically a command sender or similar.
     * @param executor The executor to run the translation task.
     * @return The translated text with placeholders replaced by their corresponding values.
     */
    default CompletableFuture<String> translateAsync(String text, O origin, Executor executor) {
        return this.translateAsync(text, this.asUser(origin), executor);
    }

    /**
     * Translates the given text using the users created from the provided origin objects asynchronously.
     *
     * @param text   The text to translate, which may contain placeholders.
     * @param origin The origin object of the primary user.
     * @param other  The origin object of the other user.
     * @return A CompletableFuture that will complete with the translated text.
     */
    default CompletableFuture<String> translateAsync(String text, O origin, O other) {
        return this.translateAsync(text, this.asUser(origin), this.asUser(other));
    }

    /**
     * Translates the given text using the users created from the provided origin objects asynchronously.
     *
     * @param text     The text to translate, which may contain placeholders.
     * @param origin   The origin object of the primary user.
     * @param other    The origin object of the other user.
     * @param executor The executor to run the translation task.
     * @return A CompletableFuture that will complete with the translated text.
     */
    default CompletableFuture<String> translateAsync(String text, O origin, O other, Executor executor) {
        return this.translateAsync(text, this.asUser(origin), this.asUser(other), executor);
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

    /**
     * Retrieves the Synapse logger for logging purposes.
     *
     * @return The SynapseLogger instance.
     */
    @NotNull
    SynapseLogger getLogger();

    /**
     * Loads neurons from the specified directory.
     * Neurons are expected to be annotated with ``@NeuronEntry`` and packaged in JAR files.
     *
     * @param directory The path to the directory containing neuron files.
     */
    void loadPluggedNeurons(Path directory);
}
