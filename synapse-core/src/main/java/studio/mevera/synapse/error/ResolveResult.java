package studio.mevera.synapse.error;

/**
 * Represents the result of resolving a throwable, indicating how the error
 * should be communicated to the user.
 *
 * <p>This record encapsulates two key pieces of information:
 * <ul>
 *     <li>The type of resolution (message, placeholder, or ignore)</li>
 *     <li>The content to display (if applicable)</li>
 * </ul>
 *
 * <p>The result type determines how the framework should handle the error:
 * <ul>
 *     <li>{@link Type#MESSAGE} - Display a direct message to the user</li>
 *     <li>{@link Type#PLACEHOLDER} - Replace with a placeholder reference (e.g., for i18n)</li>
 *     <li>{@link Type#IGNORE} - No user-facing output (error was handled silently)</li>
 * </ul>
 *
 * @param type the type of resolution result
 * @param content the content to display, or null if type is {@link Type#IGNORE}
 */
public record ResolveResult(Type type, String content) {

    /**
     * Creates a result indicating a direct message should be displayed to the user.
     *
     * @param content the message to display, should not be null
     * @return a ResolveResult with type {@link Type#MESSAGE}
     */
    public static ResolveResult message(String content) {
        return new ResolveResult(Type.MESSAGE, content);
    }

    /**
     * Creates a result indicating a placeholder reference should be used.
     * This is typically used for internationalization (i18n) where the content
     * represents a key to look up the actual message in a localized resource bundle.
     *
     * @param content the placeholder key or reference, should not be null
     * @return a ResolveResult with type {@link Type#PLACEHOLDER}
     */
    public static ResolveResult placeholder(String content) {
        return new ResolveResult(Type.PLACEHOLDER, content);
    }

    /**
     * Creates a result indicating no user-facing output is needed.
     * This is used when the error has been handled silently, such as when
     * logging the error internally or performing cleanup without notifying the user.
     *
     * @return a ResolveResult with type {@link Type#IGNORE} and null content
     */
    public static ResolveResult ignore() {
        return new ResolveResult(Type.IGNORE, null);
    }

    /**
     * Defines the type of resolution result.
     */
    public enum Type {
        /**
         * Indicates the content is a direct message to be displayed to the user.
         */
        MESSAGE,

        /**
         * Indicates the content is a placeholder reference (e.g., an i18n key)
         * that should be resolved before displaying to the user.
         */
        PLACEHOLDER,

        /**
         * Indicates no user-facing output should be produced.
         * The error was handled silently or through alternative means.
         */
        IGNORE
    }

}