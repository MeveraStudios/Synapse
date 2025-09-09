package studio.mevera.synapse.placeholder.type;

import studio.mevera.synapse.placeholder.PlaceholderOptionsBase;
import studio.mevera.synapse.placeholder.PlaceholderOptionsBuilder;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.platform.User;
import studio.mevera.synapse.util.Utilities;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class StaticPlaceholder<U extends User> implements Placeholder<U> {

    private final String name;
    private final Supplier<String> supplier;

    private final Options options;

    private String value;
    private ScheduledFuture<?> scheduledFuture;

    public StaticPlaceholder(final String name, final String result) {
        this(name, () -> result);
    }

    public StaticPlaceholder(
            final String name,
            final Supplier<String> supplier
    ) {
        this.name = name;
        this.supplier = supplier;
        this.updateValue();
        this.options = Options.DEFAULT;
        this.applyOptions();
    }

    public StaticPlaceholder(
            final String name,
            final Supplier<String> supplier,
            final Consumer<Options.Builder> options
    ) {
        this.name = name;
        this.supplier = supplier;
        options.accept(new Options.Builder());
        this.updateValue();

        final Options.Builder builder = new Options.Builder();
        options.accept(builder);
        this.options = builder.build();
        this.applyOptions();
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
        return this.value;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public void applyOptions() {
        if (this.scheduledFuture != null && !this.scheduledFuture.isCancelled()) {
            this.scheduledFuture.cancel(true);
        }

        if (!this.options.refresh()) return;

        final Runnable updateTask = () -> {
            if (options.async()) {
                CompletableFuture.runAsync(this::updateValue);
            } else {
                this.updateValue();
            }
        };

        this.scheduledFuture = Utilities.SCHEDULER.scheduleAtFixedRate(
                updateTask,
                this.options.getRefreshDelayMs(),
                this.options.getRefreshDelayMs(),
                TimeUnit.MILLISECONDS
        );
    }

    private void updateValue() {
        this.value = this.supplier.get();
    }

    public static final class Options extends PlaceholderOptionsBase {

        private static final Options DEFAULT = new Options.Builder().build();

        private final boolean async;
        private final boolean refresh;
        private final long refreshDelayMs;

        public Options(
                boolean async,
                boolean refresh,
                long refreshDelayMs
        ) {
            this.async = async;
            this.refresh = refresh;
            this.refreshDelayMs = refreshDelayMs;
        }

        /**
         * Checks if the placeholder is set to be retrieved asynchronously.
         * @return true if it is set to retrieve asynchronously, false otherwise.
         */
        public boolean async() {
            return this.async;
        }

        /**
         * Checks if the placeholder is set to be resolved again.
         * @return true if it is set to resolve again, false otherwise.
         */
        public boolean refresh() {
            return this.refresh;
        }

        /**
         * Gets the interval for resolving the placeholder again in milliseconds.
         * @return the time in milliseconds to wait before resolving again.
         */
        public long getRefreshDelayMs() {
            return refreshDelayMs;
        }

        @SuppressWarnings("all")
        public static final class Builder implements PlaceholderOptionsBuilder<Options> {

            private boolean async = false;
            private boolean refresh = false;
            private long refreshDelayMs = 20000; // Default to 20 seconds

            private Builder() {}

            public Builder refresh(boolean refresh) {
                this.refresh = refresh;
                return this;
            }

            public Builder async(boolean async) {
                this.async = async;
                return this;
            }

            public Builder delay(long amount, TimeUnit unit) {
                this.refreshDelayMs = unit.toMillis(amount);
                return this;
            }

            @Override
            public Options build() {
                return new Options(async, refresh, refreshDelayMs);
            }
        }
    }

}
