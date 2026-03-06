package studio.mevera.synapse.platform;

/**
 * A record representing platform information, including its name and version.
 *
 * @param name    The name of the platform.
 * @param version The version of the platform.
 */
public record PlatformInfo(String name, String version) implements Platform {}
