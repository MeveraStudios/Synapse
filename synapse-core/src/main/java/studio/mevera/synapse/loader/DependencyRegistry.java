package studio.mevera.synapse.loader;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DependencyRegistry {

    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    /**
     * Registers a dependency that can be injected into neuron constructors
     */
    public <T> void register(Class<T> type, T instance) {
        dependencies.put(type, instance);
    }

    /**
     * Gets a registered dependency by type, using hierarchical matching
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<T> type) {
        Object dependency = findDependency(type);
        return Optional.ofNullable((T) dependency);
    }

    /**
     * Checks if a dependency is registered (exact match only)
     */
    public boolean has(Class<?> type) {
        return dependencies.containsKey(type);
    }

    /**
     * Gets all registered dependencies (for internal use)
     */
    Map<Class<?>, Object> getAll() {
        return new HashMap<>(dependencies);
    }

    /**
     * Finds the most specific dependency for a class.
     * Checks in order:
     * 1. Exact class match
     * 2. Parent classes (walking up the hierarchy)
     * 3. Interfaces implemented by the class
     */
    @Nullable
    private Object findDependency(Class<?> clazz) {
        // 1. Check exact match
        Object dependency = dependencies.get(clazz);
        if (dependency != null) {
            return dependency;
        }

        // 2. Walk up the class hierarchy
        Class<?> current = clazz.getSuperclass();
        while (current != null) {
            dependency = dependencies.get(current);
            if (dependency != null) {
                return dependency;
            }
            current = current.getSuperclass();
        }

        // 3. Check interfaces
        for (Class<?> iface : getAllInterfaces(clazz)) {
            dependency = dependencies.get(iface);
            if (dependency != null) {
                return dependency;
            }
        }

        return null;
    }

    /**
     * Gets all interfaces implemented by a class, including inherited ones
     */
    private Set<Class<?>> getAllInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(clazz.getInterfaces()));

        // Walk up the hierarchy and collect interfaces
        Class<?> current = clazz.getSuperclass();
        while (current != null) {
            interfaces.addAll(Arrays.asList(current.getInterfaces()));
            current = current.getSuperclass();
        }

        return interfaces;
    }
}