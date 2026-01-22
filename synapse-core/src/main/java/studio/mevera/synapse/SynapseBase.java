package studio.mevera.synapse;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import studio.mevera.synapse.context.type.RelationalContext;
import studio.mevera.synapse.error.ResolveResult;
import studio.mevera.synapse.error.ThrowableResolverRegistry;
import studio.mevera.synapse.loader.DependencyRegistry;
import studio.mevera.synapse.loader.NeuronLoader;
import studio.mevera.synapse.log.SynapseLogger;
import studio.mevera.synapse.manager.NeuronRegistry;
import studio.mevera.synapse.context.type.BasicContext;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;
import studio.mevera.synapse.util.RegexUtil;

import java.nio.file.Path;
import java.util.Collection;
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
    protected final NeuronLoader<O, U, N> loader = new NeuronLoader<>(this);
    protected final DependencyRegistry dependencyRegistry = new DependencyRegistry();
    protected final ThrowableResolverRegistry<U> throwableResolverRegistry = new ThrowableResolverRegistry<>();

    protected SynapseLogger synapseLogger;

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
            final String namespace = dotIndex == -1 ? "" : tag.substring(0, dotIndex);
            final N neuron = this.neuronRegistry.getNeuron(namespace);

            if (neuron == null) {
                continue;
            }

            final String[] args = RegexUtil.extractArguments(matcher.group(2));
            final Context<U> context = new BasicContext<>(user, tag, namespace, args);

            try {
                final String key = dotIndex == -1 ? tag : tag.substring(namespace.length() + 1);
                final String replacement = neuron.onRequest(key, context);

                if (replacement != null) {
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                }
            } catch (final Throwable e) {
                final ResolveResult resolveResult = this.throwableResolverRegistry.resolve(e, context);
                if (resolveResult != null) {
                    if (resolveResult.type() == ResolveResult.Type.MESSAGE) {
                        return resolveResult.content();
                    } else if (resolveResult.type() == ResolveResult.Type.PLACEHOLDER) {
                        matcher.appendReplacement(result, Matcher.quoteReplacement(resolveResult.content()));
                        continue;
                    } else if (resolveResult.type() == ResolveResult.Type.IGNORE) {
                        continue;
                    }
                }
                throw e;
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    public String translate(final String text, final U user, final U other) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        final Matcher matcher = RegexUtil.PLACEHOLDER_PATTERN.matcher(text);
        final StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            final String tag = matcher.group(1);
            final int dotIndex = tag.indexOf('.');
            final String namespace = dotIndex == -1 ? "" : tag.substring(0, dotIndex);
            final N neuron = this.neuronRegistry.getNeuron(namespace);

            if (neuron == null) {
                continue;
            }

            final String[] args = RegexUtil.extractArguments(matcher.group(2));
            final Context<U> context = new RelationalContext<>(user, other, tag, namespace, args);

            try {
                final String key = dotIndex == -1 ? tag : tag.substring(namespace.length() + 1);
                final String replacement = neuron.onRequest(key, context);

                if (replacement != null) {
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                }
            } catch (final Throwable e) {
                final ResolveResult resolveResult = this.throwableResolverRegistry.resolve(e, context);
                if (resolveResult != null) {
                    if (resolveResult.type() == ResolveResult.Type.MESSAGE) {
                        return resolveResult.content();
                    } else if (resolveResult.type() == ResolveResult.Type.PLACEHOLDER) {
                        matcher.appendReplacement(result, Matcher.quoteReplacement(resolveResult.content()));
                        continue;
                    } else if (resolveResult.type() == ResolveResult.Type.IGNORE) {
                        continue;
                    }
                }
                throw e;
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    public void registerNeuron(final N neuron) {
        this.neuronRegistry.register(neuron);
    }

    @Override
    public @NotNull SynapseLogger getLogger() {
        if (synapseLogger == null) {
            throw new IllegalStateException("Synapse Logger has not been initialized yet");
        }
        return synapseLogger;
    }

    protected void setLogger(final @NotNull SynapseLogger logger) {
        synapseLogger = logger;
    }

    /**
     * Loads and registers all neurons from a directory
     */
    @Override
    public void loadPluggedNeurons(final Path directory) {
        try {
            final Collection<NeuronLoader.LoadedNeuron<N>> neurons = loader.loadFromDirectory(directory);
            for (NeuronLoader.LoadedNeuron<N> loaded : neurons) {
                try {
                    this.registerNeuron(loaded.neuron());
                    this.getLogger().info("Loaded neuron: " + loaded.getName() + " v" + loaded.getVersion() + " by " + loaded.getAuthor());
                } catch (Exception e) {
                    this.getLogger().error("Failed to register neuron: " + loaded.getName(), e);
                }
            }
        } catch (Exception e) {
            getLogger().error("Failed to load neurons from directory: " + directory, e);
        }
    }

    @ApiStatus.Internal
    public NeuronRegistry<U, N> getNeuronRegistry() {
        return neuronRegistry;
    }

    @ApiStatus.Internal
    public DependencyRegistry getDependencyRegistry() {
        return dependencyRegistry;
    }

}
