package studio.mevera.synapse.platform;

import java.util.UUID;

/**
 * Represents a user in the Synapse platform.
 */
public interface User {

    UUID CONSOLE_ID = UUID.nameUUIDFromBytes("synapse:console".getBytes());

    /**
     * Gets the name of the user.
     *
     * @return the name of the user.
     */
    String name();

    /**
     * Gets the unique identifier of the user.
     *
     * @return the UUID of the user.
     */
    UUID uniqueId();

    /**
     * Gets the origin of the user, which can be a player, console, or other types of users.
     *
     * @return the origin object of the user.
     */
    Object origin();

    /**
     * Checks if the user is a player.
     *
     * @return true if the user is a player, false otherwise.
     */
    boolean isPlayer();

    /**
     * Checks if the user is the console.
     *
     * @return true if the user is the console, false otherwise.
     */
    boolean isConsole();

    /**
     * Checks if the user is connected.
     *
     * @return true if the user is online/is a console, false otherwise.
     */
    boolean isConnected();

    /**
     * Caches a value for the user with a specified key and arguments.
     *
     * @param key       the cache key
     * @param args      the arguments associated with the key
     * @param value     the value to cache
     * @param ttlMillis the time-to-live in milliseconds for the cached value
     */
    void cache(
            String key,
            String[] args,
            String value,
            long ttlMillis
    );

    /**
     * Retrieves a cached value for the user with a specified key and arguments.
     *
     * @param key  the cache key
     * @param args the arguments associated with the key
     * @return the cached value, or null if not found
     */
    String getCachedValue(
            String key,
            String[] args
    );

    /**
     * Clears the cache for the user.
     */
    void invalidateCache();

    /**
     * Invalidates the cache for a specific key.
     *
     * @param key the cache key to invalidate
     */
    void invalidateCache(String key);
}
