package studio.mevera.synapse.manager;

import studio.mevera.synapse.platform.Neuron;

import java.nio.file.Path;

public record RegisteredNeuron<N extends Neuron<?>>(
        N neuron,
        boolean internal,
        ClassLoader classLoader,
        Path jarPath
) {}