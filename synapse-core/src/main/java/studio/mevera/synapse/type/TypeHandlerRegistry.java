package studio.mevera.synapse.type;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for type handlers that convert objects to strings.
 * Supports inheritance-based matching and caches lookups for performance.
 */
@SuppressWarnings("unchecked")
public class TypeHandlerRegistry {

    /**
     * Simple functional interface for handler functions.
     */
    @FunctionalInterface
    public interface HandlerFunction<T> {
        String handle(T value);
    }

    private final Map<Type, TypeHandler<?>> handlers = new ConcurrentHashMap<>();
    private final Map<Class<?>, TypeHandler<?>> cache = new ConcurrentHashMap<>();

    /**
     * Registers a type handler with type information.
     *
     * @param handler the handler with getType() implementation
     */
    public <T> void register(TypeHandler<T> handler) {
        handlers.put(handler.getType(), handler);
        clearCache();
    }

    /**
     * Registers a simple handler function for a specific type.
     *
     * @param type the type to handle
     * @param handler the handler function
     */
    public <T> void register(Type type, HandlerFunction<T> handler) {
        TypeHandler<T> wrapped = new TypeHandler<>() {
            @Override
            public Type getType() {
                return type;
            }

            @Override
            public String handle(T value) {
                return handler.handle(value);
            }
        };
        handlers.put(type, wrapped);
        clearCache();
    }

    /**
     * Registers a handler for a class.
     *
     * @param clazz the class to handle
     * @param handler the handler function
     */
    public <T> void register(Class<T> clazz, HandlerFunction<T> handler) {
        register((Type) clazz, handler);
    }

    /**
     * Handles a value by finding the appropriate handler.
     *
     * @param value the value to handle
     * @return the string representation, or null if value is null or no handler found
     */
    @Nullable
    public String handle(@Nullable Object value) {
        if (value == null) {
            return null;
        }

        Class<?> valueClass = value.getClass();

        // Check cache first
        TypeHandler<?> cachedHandler = cache.get(valueClass);
        if (cachedHandler != null) {
            return ((TypeHandler<Object>) cachedHandler).handle(value);
        }

        // Find handler
        TypeHandler<?> handler = findHandler(valueClass);
        if (handler != null) {
            cache.put(valueClass, handler);
            return ((TypeHandler<Object>) handler).handle(value);
        }

        return null;
    }

    /**
     * Finds the most specific handler for a class.
     * Checks in order:
     * 1. Exact class match
     * 2. Parent classes (walking up the hierarchy)
     * 3. Interfaces implemented by the class
     */
    @Nullable
    private TypeHandler<?> findHandler(Class<?> clazz) {
        // 1. Check exact match
        TypeHandler<?> handler = handlers.get(clazz);
        if (handler != null) {
            return handler;
        }

        // Special-case: arrays of objects should use a handler registered for Object[].class
        if (clazz.isArray()) {
            TypeHandler<?> arrayHandler = handlers.get(Object[].class);
            if (arrayHandler != null) {
                return arrayHandler;
            }
        }

        // 2. Walk up the class hierarchy
        Class<?> current = clazz.getSuperclass();
        while (current != null) {
            handler = handlers.get(current);
            if (handler != null) {
                return handler;
            }
            current = current.getSuperclass();
        }

        // 3. Check interfaces
        for (Class<?> iface : getAllInterfaces(clazz)) {
            handler = handlers.get(iface);
            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    /**
     * Gets all interfaces implemented by a class, including parent interfaces.
     */
    private Set<Class<?>> getAllInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new LinkedHashSet<>();

        while (clazz != null) {
            for (Class<?> iface : clazz.getInterfaces()) {
                interfaces.add(iface);
                interfaces.addAll(getAllInterfaces(iface));
            }
            clazz = clazz.getSuperclass();
        }

        return interfaces;
    }

    /**
     * Clears the lookup cache.
     * Called automatically when a new handler is registered.
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Removes a handler for a specific type.
     */
    public void unregister(Type type) {
        handlers.remove(type);
        clearCache();
    }

    /**
     * Gets all registered types.
     */
    public Set<Type> getRegisteredTypes() {
        return Collections.unmodifiableSet(handlers.keySet());
    }

    /**
     * Checks if a handler is registered for a type.
     */
    public boolean hasHandler(Type type) {
        return handlers.containsKey(type);
    }

    /**
     * Checks if a handler is registered for a type.
     */
    public boolean hasHandler(Object type) {
        return hasHandler(type.getClass());
    }

    /**
     * Gets the handler for a specific type (exact match only).
     */
    @Nullable
    public TypeHandler<?> getHandler(Type type) {
        return handlers.get(type);
    }
}