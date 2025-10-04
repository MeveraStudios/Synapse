package studio.mevera.synapse.error;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.platform.User;

public interface ThrowableResolver<T extends Throwable, U extends User> {

    /**
     * Resolves the given throwable within the provided context.
     * This method is invoked when a throwable of type {@code T} occurs during
     * command execution, allowing for custom handling such as user notification,
     * logging, cleanup operations, or alternative command flows.
     *
     * <p>Implementations should consider:
     * <ul>
     *     <li>Not throwing additional exceptions unless absolutely necessary</li>
     *     <li>Providing meaningful feedback to the user when appropriate</li>
     *     <li>Performing any necessary cleanup or state restoration</li>
     *     <li>Logging errors for debugging purposes when needed</li>
     * </ul>
     *
     * <p>The resolution process should be non-blocking and efficient, as it's
     * part of the critical command execution path.
     *
     * @param throwable the throwable that occurred during command execution, never {@code null}
     * @param context the execution context containing the user, command state, and other
     * relevant information at the time the throwable occurred, never {@code null}
     *
     * @see Context#user()
     * @return ResolveResult indicating how the error should be communicated to the user
     */
    ResolveResult resolve(final T throwable, Context<U> context);

}
