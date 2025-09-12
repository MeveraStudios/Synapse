package studio.mevera.synapse.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Utilities {

    public static final String HYPHEN = "#########################################################";
    public static final String ASCII_ART = """
              ____                                  \s
             / ___| _   _ _ __   __ _ _ __  ___  ___\s
             \\___ \\| | | | '_ \\ / _` | '_ \\/ __|/ _ \\
              ___) | |_| | | | | (_| | |_) \\__ \\  __/
             |____/ \\__, |_| |_|\\__,_| .__/|___/\\___|
                    |___/            |_|            \s
            """;

    public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(3);

    private Utilities() {
        // Prevent instantiation
    }

    /**
     * Finds any {@link Class} of the provided paths
     *
     * @param paths all possible class paths
     * @return false if the {@link Class} was NOT found
     */
    public static boolean findClass(final String... paths) {
        for (final String path : paths) {
            if (getClass(path.replace("{}", ".")) != null) return true;
        }
        return false;
    }

    /**
     * A nullable {@link Class#forName(String)} instead of throwing exceptions
     *
     * @return null if the {@link Class} was NOT found
     */
    public static Class<?> getClass(@NotNull final String path) {
        try {
            return Class.forName(path);
        } catch (final Exception ignored) {
            return null;
        }
    }

}
