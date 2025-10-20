package studio.mevera.synapse.placeholder;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.platform.User;

public interface Placeholder<U extends User> {

    /**
     * Gets the name of the placeholder.
     *
     * @return The name of the placeholder.
     */
    String name();

    /**
     * Gets the options of the placeholder.
     *
     * @return The options of the placeholder.
     */
    PlaceholderOptions options();

    /**
     * Resolves the placeholder value based on the given context.
     *
     * @param context The context to resolve the placeholder.
     * @return The resolved value of the placeholder.
     */
    Object resolve(Context<U> context);

    /**
     * Indicates if the placeholder is static.
     *
     * @return true if the placeholder is static, false otherwise.
     */
    default boolean isStatic() {
        return false;
    }

    /**
     * Indicates if the placeholder is contextual.
     *
     * @return true if the placeholder is contextual, false otherwise.
     */
    default boolean isContextual() {
        return false;
    }

    /**
     * Indicates if the placeholder is relational.
     *
     * @return true if the placeholder is relational, false otherwise.
     */
    default boolean isRelational() {
        return false;
    }

    /**
     * Updates the options of the placeholder.
     */
    default void applyOptions() {
        // Default implementation does nothing
    }
}
