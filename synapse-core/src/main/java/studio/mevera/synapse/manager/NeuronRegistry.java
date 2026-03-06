package studio.mevera.synapse.manager;

import studio.mevera.synapse.platform.Namespace;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URLClassLoader;
import java.util.*;

/**
 * NeuronRegistry is responsible for managing the registration and retrieval of neurons.
 * It allows neurons to be registered with a specific namespace and provides methods to retrieve them.
 *
 * @param <U> the type of User associated with the neurons
 * @param <N> the type of Neuron associated with the User
 */
public class NeuronRegistry<U extends User, N extends Neuron<U>> {

    private final Map<String, RegisteredNeuron<N>> neurons = new HashMap<>();
    private final Set<RegisteredNeuron<N>> uniqueNeurons = new HashSet<>();

    /** Register an internally bundled neuron (no jar/classloader tracking). */
    public void registerInternal(final N neuron) {
        register(neuron, true, null, null);
    }

    /** Register an externally loaded neuron with its loader metadata. */
    public void registerExternal(final N neuron, final ClassLoader loader, final Path jarPath) {
        register(neuron, false, loader, jarPath);
    }

    private void register(final N neuron, final boolean internal, final ClassLoader loader, final Path jarPath) {
        final Namespace namespace = neuron.namespace();
        for (final String ns : namespace.getNames()) {
            if (neurons.containsKey(ns)) {
                throw new IllegalArgumentException("Neuron with namespace " + ns + " is already registered.");
            }
        }

        RegisteredNeuron<N> registered = new RegisteredNeuron<>(neuron, internal, loader, jarPath);
        for (final String ns : namespace.getNames()) {
            neurons.put(ns, registered);
        }
        uniqueNeurons.add(registered);
    }

    /**
     * Retrieves a neuron by its namespace.
     *
     * @param namespace the namespace of the neuron
     * @return the neuron associated with the given namespace, or null if not found
     */
    public N getNeuron(final String namespace) {
        RegisteredNeuron<N> registered = neurons.get(namespace);
        return registered == null ? null : registered.neuron();
    }

    /**
     * Retrieves registration info by namespace.
     */
    public RegisteredNeuron<N> getRegistration(final String namespace) {
        return neurons.get(namespace);
    }

    /**
     * Retrieves all registered neurons.
     *
     * @return a set of all neurons
     */
    public Collection<N> getNeurons() {
        return uniqueNeurons.stream().map(RegisteredNeuron::neuron).toList();
    }

    /**
     * Retrieves all registrations.
     */
    public Collection<RegisteredNeuron<N>> getRegistrations() {
        return Collections.unmodifiableCollection(uniqueNeurons);
    }

    /**
     * Unregisters a neuron by namespace. Optionally deletes its jar.
     *
     * @param namespace the namespace to remove
     * @param deleteJar whether to delete the associated jar if external
     * @return true if a neuron was removed
     */
    public boolean unregister(final String namespace, final boolean deleteJar) {
        RegisteredNeuron<N> registered = neurons.get(namespace);
        if (registered == null) {
            return false;
        }

        // remove all namespace aliases
        for (String ns : registered.neuron().namespace().getNames()) {
            neurons.remove(ns);
        }
        uniqueNeurons.remove(registered);

        if (!registered.internal()) {
            closeQuietly(registered.classLoader());
            if (deleteJar && registered.jarPath() != null) {
                try {
                    Files.deleteIfExists(registered.jarPath());
                } catch (Throwable ignored) {
                    // best-effort
                }
            }
        }
        return true;
    }

    /** Closes URLClassLoader quietly. */
    private void closeQuietly(ClassLoader loader) {
        if (loader instanceof URLClassLoader urlClassLoader) {
            try {
                urlClassLoader.close();
            } catch (Throwable ignored) {
            }
        }
    }

}
