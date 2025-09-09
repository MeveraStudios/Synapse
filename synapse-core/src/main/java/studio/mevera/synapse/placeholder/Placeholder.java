package studio.mevera.synapse.placeholder;

import studio.mevera.synapse.platform.User;

public interface Placeholder<U extends User> {

    String name();
    PlaceholderOptions options();
    String resolve(Context<U> context);

    default boolean isStatic() {
        return false;
    }

    default boolean isContextual() {
        return false;
    }

    /**
     * Updates the options of the placeholder.
     */
    default void applyOptions() {
        // Default implementation does nothing
    }
}
