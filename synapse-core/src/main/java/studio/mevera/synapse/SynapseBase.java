package studio.mevera.synapse;

import studio.mevera.synapse.manager.NeuronRegistry;
import studio.mevera.synapse.placeholder.ContextBase;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;
import studio.mevera.synapse.util.RegexUtil;

import java.util.regex.Matcher;

/**
 * Abstract base class for Synapse
 * providing common functionality for translating text
 * and managing neurons.
 *
 * @param <O> the type of origin (e.g., CommandSender)
 * @param <U> the type of user (e.g., BukkitUser)
 * @param <N> the type of neuron (e.g., BukkitNeuron)
 */
public abstract class SynapseBase<O, U extends User, N extends Neuron<U>> implements Synapse<O, U, N> {

    protected final NeuronRegistry<U, N> neuronRegistry = new NeuronRegistry<>();

    @Override
    public String translate(final String text, final U user) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        final Matcher matcher = RegexUtil.PLACEHOLDER_PATTERN.matcher(text);
        final StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            final String tag = matcher.group(1);
            final int dotIndex = tag.indexOf('.');
            final String namespace = dotIndex == -1 ? tag : tag.substring(0, dotIndex);
            final N neuron = this.neuronRegistry.getNeuron(namespace);

            if (neuron == null) {
                continue;
            }

            final String[] args = RegexUtil.extractArguments(matcher.group(2));
            final Context<U> context = new ContextBase<>(user, tag, namespace, args);
            final String replacement = neuron.onRequest(tag.substring(namespace.length() + 1), context);

            if (replacement != null) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    public void registerNeuron(final N neuron) {
        this.neuronRegistry.register(neuron);
    }

}
