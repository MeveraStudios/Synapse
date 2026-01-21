package studio.mevera.synapse.log;

/**
 * Synapse's basic logging contract.
 * <p>
 * Used across the project to print messages in different "importance levels"
 * (info, warnings, debug, errors).
 * <p>
 * Where the messages actually go is up to the implementation:
 * console, file, external logging library... whatever you plug in.
 */
public interface SynapseLogger {

    /**
     * For normal updates and "everything is fine" messages.
     *
     * @param message the message to log
     */
    void info(String message);

    /**
     * For suspicious or unusual situations that aren't fatal (yet).
     * Think: something went wrong, but Synapse can still continue safely.
     *
     * @param message the warning message to log
     */
    void warn(String message);

    /**
     * For failures and serious problems.
     * Use this when something *did* go wrong and needs attention.
     *
     * @param message the error message to log
     */
    void error(String message);

    /**
     * For failures with extra context (aka: the stack trace).
     * This is what you want when catching exceptions and reporting them properly.
     *
     * @param message the error message to log
     * @param throwable the exception/error that caused this log (can be {@code null})
     */
    void error(String message, Throwable throwable);

    /**
     * For nerdy diagnostics 👀
     * Use it for tracking internals, performance hints, or debugging logic.
     * Usually disabled in production unless explicitly enabled.
     *
     * @param message the debug message to log
     */
    void debug(String message);

    /**
     * Same as {@link #debug(String)} but with extra context.
     * Great when you want the stack trace, but you're not treating it as a fatal error.
     *
     * @param message the debug message to log
     * @param throwable the related throwable (can be {@code null})
     */
    void debug(String message, Throwable throwable);

}
