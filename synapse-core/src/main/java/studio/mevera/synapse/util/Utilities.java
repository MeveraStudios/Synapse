package studio.mevera.synapse.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Utilities {

    private final String ASCII_ART = """
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

}
