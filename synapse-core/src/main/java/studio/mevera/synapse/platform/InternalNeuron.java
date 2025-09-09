package studio.mevera.synapse.platform;

import studio.mevera.synapse.internal.EvalPlaceholder;

public interface InternalNeuron<U extends User> extends Neuron<U> {

    default void registerInternalPlaceholders() {
        this.register(new EvalPlaceholder<>());
    }

}
