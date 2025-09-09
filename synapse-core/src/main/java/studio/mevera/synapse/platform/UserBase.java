package studio.mevera.synapse.platform;

import studio.mevera.synapse.util.ArrayKey;
import studio.mevera.synapse.util.ExpiringTable;

public abstract class UserBase implements User {

    public final ExpiringTable<String, ArrayKey, String> cache = new ExpiringTable<>();

    @Override
    public void cache(
            final String key,
            final String[] args,
            final String value,
            final long ttlMillis
    ) {
        this.cache(key, ArrayKey.of(args), value, ttlMillis);
    }

    public void cache(
            final String key,
            final ArrayKey args,
            final String value,
            final long ttlMillis
    ) {
        this.cache.put(key, args, value, ttlMillis);
    }

    @Override
    public String getCachedValue(
            final String key,
            final String[] args
    ) {
        return this.getCachedValue(key, ArrayKey.of(args));
    }

    public String getCachedValue(
            final String key,
            final ArrayKey args
    ) {
        return this.cache.get(key, args);
    }

    @Override
    public void invalidateCache() {
        this.cache.clear();
    }

    @Override
    public void invalidateCache(String key) {
        this.cache.remove(key);
    }

}
