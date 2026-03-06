package studio.mevera.synapse.platform;

/**
 * Represents a platform on which the plugin is running.
 */
public interface Platform {

    /**
     * Gets the name of the platform.
     *
     * @return The name of the platform.
     */
    String name();

    /**
     * Gets the version of the platform.
     *
     * @return The version of the platform.
     */
    String version();

}
