package studio.mevera.synapse.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A two-dimensional table-like structure that maps pairs of keys to values.
 * This class allows you to store and retrieve values based on two keys (K1 and K2).
 * It supports expiration of values, meaning that values can be set to expire after a specified time-to-live (TTL).
 *
 * @param <K1> the type of the first key
 * @param <K2> the type of the second key
 * @param <V>  the type of the value
 */
public class ExpiringTable<K1, K2, V> {

    // A backing map that holds the values, where each key1 maps to another map of key2 to value.
    private final Map<K1, Map<K2, ExpiringValue<V>>> backingMap = new ConcurrentHashMap<>();

    /**
     * Puts a value into the table associated with the given pair of keys.
     *
     * @param key1  the first key
     * @param key2  the second key
     * @param value the value to be stored
     * @param ttlMillis the time-to-live in milliseconds for the value
     */
    public void put(
            final K1 key1,
            final K2 key2,
            final V value,
            final long ttlMillis
    ) {
        backingMap
                .computeIfAbsent(key1, k -> new ConcurrentHashMap<>())
                .put(key2, new ExpiringValue<>(value, ttlMillis));
    }

    /**
     * Retrieves the value associated with the given pair of keys.
     *
     * @param key1 the first key
     * @param key2 the second key
     * @return the value associated with the keys, or null if not found
     */
    public V get(K1 key1, K2 key2) {
        final Map<K2, ExpiringValue<V>> inner = backingMap.get(key1);
        if (inner == null) return null;

        final ExpiringValue<V> entry = inner.get(key2);
        if (entry == null || entry.isExpired()) {
            inner.remove(key2);
            if (inner.isEmpty()) backingMap.remove(key1);
            return null;
        }

        return entry.value;
    }

    /**
     * Removes all values associated with the given first key.
     *
     * @param key1 the first key
     * @return the removed map of second keys to values, or null if not found
     */
    public Map<K2, ExpiringValue<V>> remove(K1 key1) {
        return backingMap.remove(key1);
    }

    /**
     * Removes the value associated with the given pair of keys.
     *
     * @param key1 the first key
     * @param key2 the second key
     * @return the removed value, or null if not found
     */
    public V remove(K1 key1, K2 key2) {
        Map<K2, ExpiringValue<V>> inner = backingMap.get(key1);
        if (inner == null) return null;
        ExpiringValue<V> removed = inner.remove(key2);
        if (inner.isEmpty()) backingMap.remove(key1);
        return removed != null ? removed.value : null;
    }

    /**
     * Checks if the table contains a value for the given pair of keys.
     *
     * @param key1 the first key
     * @param key2 the second key
     * @return true if the value exists, false otherwise
     */
    public boolean contains(final K1 key1, final K2 key2) {
        Map<K2, ExpiringValue<V>> inner = backingMap.get(key1);
        ExpiringValue<V> val = inner != null ? inner.get(key2) : null;
        return val != null && !val.isExpired();
    }

    /**
     * Retrieves all values associated with the first key.
     *
     * @param key1 the first key
     * @return a map of second keys to values, or an empty map if no values are found
     */
    public Map<K2, ExpiringValue<V>> row(K1 key1) {
        return backingMap.getOrDefault(key1, Map.of());
    }

    /**
     * Returns the entire backing map, which contains all key-value pairs.
     *
     * @return a map where each key is a first key and the value is another map of second keys to values
     */
    public Map<K1, Map<K2, ExpiringValue<V>>> asMap() {
        return backingMap;
    }

    /**
     * Returns a set of all first keys in the table.
     *
     * @return a set of first keys
     */
    public Set<K1> rowKeySet() {
        return backingMap.keySet();
    }

    /**
     * Clears the table, removing all entries.
     */
    public void clear() {
        backingMap.clear();
    }

    /**
     * Returns the number of entries in the table.
     * This counts all key-value pairs across all first keys.
     *
     * @return the total number of entries
     */
    public int size() {
        return backingMap.values().stream().mapToInt(Map::size).sum();
    }

    /**
     * Cleans up expired entries in the table.
     * This method iterates through all entries and removes those that have expired.
     */
    public void cleanupExpired() {
        for (final K1 key1 : backingMap.keySet()) {
            Map<K2, ExpiringValue<V>> inner = backingMap.get(key1);
            if (inner == null) continue;

            inner.entrySet().removeIf(e -> e.getValue().isExpired());

            if (inner.isEmpty()) backingMap.remove(key1);
        }
    }

    /**
     * A simple class to hold a value with an expiration time.
     * This is used for caching purposes where values may expire after a certain time.
     *
     * @param <V> the type of the value
     */
    public static class ExpiringValue<V> {
        final V value;
        final long expiryMillis;

        ExpiringValue(V value, long ttlMillis) {
            this.value = value;
            this.expiryMillis = ttlMillis >= 0
                    ? System.currentTimeMillis() + ttlMillis
                    : -1L;
        }

        public boolean isExpired() {
            return expiryMillis != -1 && System.currentTimeMillis() > expiryMillis;
        }

        public V getValue() {
            return value;
        }
    }

}
