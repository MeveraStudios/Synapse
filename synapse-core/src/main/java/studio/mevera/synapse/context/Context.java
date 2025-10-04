package studio.mevera.synapse.context;

import studio.mevera.synapse.platform.User;


public interface Context<U extends User> {

    /**
     * Returns the user associated with this context.
     *
     * @return the user of the context
     */
    U user();

    /**
     * Returns the tag associated with this context.
     *
     * @return the tag of the context
     */
    String tag();

    /**
     * Returns the user associated with this context.
     *
     * @return the user of the context
     */
    String namespace();

    /**
     * Returns the arguments associated with this context.
     *
     * @return the arguments of the context
     */
    String[] arguments();

    /**
     * Checks if the context is relational.
     *
     * @return true if the context is relational, false otherwise
     */
    default boolean isRelational() {
        return false;
    }

    /**
     * Overrides the message associated with this context.
     *
     * @param replacement the new message to set
     */
    void overrideMessage(final String replacement);

    /**
     * Overrides the placeholder associated with this context.
     *
     * @param replacement the new message to set
     */
    void overridePlaceholder(final String replacement);

}
