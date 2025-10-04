package studio.mevera.synapse.error;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.platform.User;

import java.util.*;

/**
 * ThrowableResolverRegistry manages the registration and retrieval of throwable resolvers.
 * It allows resolvers to be registered for specific throwable types and provides hierarchical
 * resolution by searching up the exception class hierarchy.
 *
 * @param <U> the type of User associated with the resolvers
 */
public class ThrowableResolverRegistry<U extends User> {

    private final Map<Class<? extends Throwable>, ThrowableResolver<?, U>> resolvers = new HashMap<>();

    /**
     * Registers a resolver for a specific throwable type.
     *
     * @param throwableClass the class of the throwable to handle
     * @param resolver the resolver to register
     * @param <T> the type of throwable
     * @throws IllegalArgumentException if a resolver for this throwable type is already registered
     */
    public <T extends Throwable> void register(final Class<T> throwableClass, final ThrowableResolver<T, U> resolver) {
        if (resolvers.containsKey(throwableClass)) {
            throw new IllegalArgumentException("Resolver for throwable type " + throwableClass.getName() + " is already registered.");
        }
        resolvers.put(throwableClass, resolver);
    }

    /**
     * Retrieves a resolver for the exact throwable type.
     *
     * @param throwableClass the class of the throwable
     * @param <T> the type of throwable
     * @return the resolver for the given throwable type, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> ThrowableResolver<T, U> getResolver(final Class<T> throwableClass) {
        return (ThrowableResolver<T, U>) resolvers.get(throwableClass);
    }

    /**
     * Finds the most specific resolver for a throwable by searching up the class hierarchy.
     * This method first looks for an exact match, then checks superclasses.
     *
     * @param throwable the throwable instance
     * @param <T> the type of throwable
     * @return the most specific resolver found, or null if no resolver matches
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> ThrowableResolver<T, U> findResolver(final T throwable) {
        Class<?> currentClass = throwable.getClass();
        
        while (currentClass != null && Throwable.class.isAssignableFrom(currentClass)) {
            ThrowableResolver<?, U> resolver = resolvers.get(currentClass);
            if (resolver != null) {
                return (ThrowableResolver<T, U>) resolver;
            }
            currentClass = currentClass.getSuperclass();
        }

        if (throwable instanceof ThrowableResolver<?,?>) {
            return (ThrowableResolver<T, U>) throwable;
        }

        return null;
    }

    /**
     * Resolves a throwable using the most specific registered resolver.
     * If no resolver is found, this method does nothing.
     *
     * @param throwable the throwable to resolve
     * @param context the execution context
     * @param <T> the type of throwable
     */
    public <T extends Throwable> ResolveResult resolve(final T throwable, final Context<U> context) {
        ThrowableResolver<T, U> resolver = findResolver(throwable);
        if (resolver != null) {
            return resolver.resolve(throwable, context);
        }
        return null;
    }

    /**
     * Retrieves all registered resolvers.
     *
     * @return an unmodifiable collection of all registered resolvers
     */
    public Collection<ThrowableResolver<?, U>> getResolvers() {
        return Collections.unmodifiableCollection(new HashSet<>(resolvers.values()));
    }

    /**
     * Checks if a resolver is registered for the exact throwable type.
     *
     * @param throwableClass the class of the throwable
     * @return true if a resolver is registered for this type, false otherwise
     */
    public boolean hasResolver(final Class<? extends Throwable> throwableClass) {
        return resolvers.containsKey(throwableClass);
    }

    /**
     * Unregisters a resolver for a specific throwable type.
     *
     * @param throwableClass the class of the throwable
     * @return the unregistered resolver, or null if none was registered
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> ThrowableResolver<T, U> unregister(final Class<T> throwableClass) {
        return (ThrowableResolver<T, U>) resolvers.remove(throwableClass);
    }

}