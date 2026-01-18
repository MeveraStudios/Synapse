package studio.mevera.synapse;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import studio.mevera.synapse.platform.AdventureNeuronBase;
import studio.mevera.synapse.platform.User;

public abstract class AdventureSynapseBase<O, U extends User, N extends AdventureNeuronBase<U>> extends SynapseBase<O, U, N> {

    /**
     * Constructs a TagResolver that aggregates tags from all registered Adventure neurons.
     *
     * @return A TagResolver containing tags from all Adventure neurons.
     */
    public TagResolver asTagResolver() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final AdventureNeuronBase<U> neuron : this.neuronRegistry.getNeurons()) {
            builder.resolver(neuron.getOrFormAdventureTag());
        }
        return builder.build();
    }

}
