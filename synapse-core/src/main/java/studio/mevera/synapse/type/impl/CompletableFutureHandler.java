package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureHandler implements TypeHandler<CompletableFuture<?>> {

    private final TypeHandlerRegistry registry;

    public CompletableFutureHandler(TypeHandlerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Type getType() {
        return CompletableFuture.class;
    }

    @Override
    public String handle(CompletableFuture<?> future) {
        if (future.isCancelled()) {
            return null; // Or "[cancelled]"
        }

        if (future.isCompletedExceptionally()) {
            return null; // Or "[error]"
        }

        if (future.isDone()) {
            try {
                Object value = future.getNow(null);
                return registry.handle(value);
            } catch (Exception e) {
                return null;
            }
        }

        return null; // Or "[pending]"
    }
}