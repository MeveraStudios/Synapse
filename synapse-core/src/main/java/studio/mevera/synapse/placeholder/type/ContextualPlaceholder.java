package studio.mevera.synapse.placeholder.type;

import studio.mevera.synapse.placeholder.PlaceholderOptionsBase;
import studio.mevera.synapse.placeholder.PlaceholderOptionsBuilder;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.platform.User;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ContextualPlaceholder<U extends User> implements Placeholder<U> {

    private final String name;
    private final ResolvingFunction<U> value;
    private final Options options;

    public ContextualPlaceholder(final String name, final ResolvingFunction<U> value) {
        this.name = name;
        this.value = value;
        this.options = Options.DEFAULT;
    }

    public ContextualPlaceholder(
            final String name,
            final ResolvingFunction<U> value,
            final Consumer<Options.Builder> options
    ) {
        this.name = name;
        this.value = value;

        final Options.Builder builder = new Options.Builder();
        options.accept(builder);
        this.options = builder.build();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Options options() {
        return this.options;
    }

    @Override
    public String resolve(final Context<U> context) {
        Objects.requireNonNull(context);

        if (this.options.cache()) {
            final U user = context.user();
            final String key = context.namespace() + ":" + this.name;

            final String cachedValue = user.getCachedValue(key, context.arguments());
            if (cachedValue != null) {
                return cachedValue;
            }

            final String resolvedValue = this.value.resolve(context);
            user.cache(key, context.arguments(), resolvedValue, this.options.cacheTTLMillis());
            return resolvedValue;
        }

        return this.value.resolve(context);
    }

    public interface ResolvingFunction<U extends User> {
        String resolve(final Context<U> context);
    }

    @Override
    public boolean isContextual() {
        return true;
    }

    public static final class Options extends PlaceholderOptionsBase {

        private static final Options DEFAULT = new Builder().build();

        private final boolean cache;
        private final long ttlMillis;

        /**
         * Constructs a new Options instance with the specified cache setting and TTL.
         *
         * @param cache     whether to enable caching
         * @param ttlMillis the time-to-live for the cache in milliseconds
         */
        public Options(boolean cache, long ttlMillis) {
            this.cache = cache;
            this.ttlMillis = ttlMillis;
        }

        /**
         * Returns whether this placeholder should cache its resolved value.
         *
         * @return true if caching is enabled, false otherwise
         */
        public boolean cache() {
            return this.cache;
        }

        /**
         * Returns the time-to-live (TTL) for the cache in milliseconds.
         *
         * @return the TTL in milliseconds
         */
        public long cacheTTLMillis() {
            return this.ttlMillis;
        }

        @SuppressWarnings("all")
        public static final class Builder implements PlaceholderOptionsBuilder<Options> {

            private boolean cache = false;
            private long ttlMillis = 20000; // 20 seconds default

            private Builder() {}

            public Builder cache(boolean cache) {
                this.cache = cache;
                return this;
            }

            public Builder cacheTTL(long amount, TimeUnit unit) {
                this.ttlMillis = unit.toMillis(amount);
                return this;
            }

            @Override
            public Options build() {
                return new Options(cache, ttlMillis);
            }
        }
    }

}
