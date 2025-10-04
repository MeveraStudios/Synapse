package studio.mevera.synapse.manager;

import studio.mevera.synapse.platform.Namespace;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;

import java.util.*;

/**
 * NeuronRegistry is responsible for managing the registration and retrieval of neurons.
 * It allows neurons to be registered with a specific namespace and provides methods to retrieve them.
 *
 * @param <U> the type of User associated with the neurons
 * @param <N> the type of Neuron associated with the User
 */
public class NeuronRegistry<U extends User, N extends Neuron<U>> {

    private final Map<String, N> neurons = new HashMap<>();
    private final Set<N> uniqueNeurons = new HashSet<>();

    /**
     * Registers a neuron with the given namespace.
     *
     * @param neuron the neuron to register
     */
    public void register(final N neuron) {
        final Namespace namespace = neuron.namespace();
        for (final String ns : namespace.getNames()) {
            if (neurons.containsKey(ns)) {
                throw new IllegalArgumentException("Neuron with namespace " + ns + " is already registered.");
            }
        }

        for (final String ns : namespace.getNames()) {
            neurons.put(ns, neuron);
        }
        uniqueNeurons.add(neuron);
    }

    /**
     * Retrieves a neuron by its namespace.
     *
     * @param namespace the namespace of the neuron
     * @return the neuron associated with the given namespace, or null if not found
     */
    public N getNeuron(final String namespace) {
        return neurons.get(namespace);
    }

    /**
     * Retrieves all registered neurons.
     *
     * @return a set of all neurons
     */
    public Collection<N> getNeurons() {
        return Collections.unmodifiableCollection(uniqueNeurons);
    }

}
