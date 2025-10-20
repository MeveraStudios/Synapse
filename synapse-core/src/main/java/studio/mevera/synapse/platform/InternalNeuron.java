package studio.mevera.synapse.platform;

public interface InternalNeuron<U extends User> extends Neuron<U> {

    default void registerInternalPlaceholders() {
        this.register("name", context -> context.user().name());
        //this.register(new studio.mevera.synapse.internal.EvalPlaceholder<>());
    }

}
