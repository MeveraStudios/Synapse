package studio.mevera.synapse.placeholder.type;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.RelationalContext;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.PlaceholderOptionsBase;
import studio.mevera.synapse.placeholder.PlaceholderOptionsBuilder;
import studio.mevera.synapse.platform.User;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class RelationalPlaceholder<U extends User> implements Placeholder<U> {

    private final String name;
    private final Function<RelationalContext<U>, Object> value;
    private final Options options;

    public RelationalPlaceholder(String name, Function<RelationalContext<U>, Object> value) {
        this.name = name;
        this.value = value;
        this.options = Options.DEFAULT;
    }

    public RelationalPlaceholder(String name, Function<RelationalContext<U>, Object> value, Consumer<Options.Builder> options) {
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
    public Object resolve(Context<U> unverifiedContext) {
        Objects.requireNonNull(unverifiedContext);
        if (!unverifiedContext.isRelational()) {
            return null;
        }

        final var context = (RelationalContext<U>) unverifiedContext;
        final U other = context.other();
        if (other == null) {
            return null;
        }

        if (this.options.cache()) {
            final U user = context.user();
            final String key = context.namespace() + ":" + this.name + ":" + other.uniqueId();

            final Object cachedValue = user.getCachedValue(key, context.arguments());
            if (cachedValue != null) {
                return cachedValue;
            }

            final Object resolvedValue = this.value.apply(context);
            user.cache(key, context.arguments(), resolvedValue, this.options.cacheTTLMillis());
            return resolvedValue;
        }

        return this.value.apply(context);
    }

    @Override
    public boolean isRelational() {
        return true;
    }

    public static final class Options extends PlaceholderOptionsBase {

        private static final Options DEFAULT = new Options.Builder().build();

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
