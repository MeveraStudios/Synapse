package studio.mevera.synapse.platform;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import studio.mevera.synapse.VelocitySynapse;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.BasicContex;

import java.util.LinkedList;
import java.util.List;

public class VelocityNeuron extends AdventureNeuronBase<VelocityUser> {

    private final PluginContainer plugin;

    public VelocityNeuron(final PluginContainer plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

    @Override
    protected Context<VelocityUser> toContext(
            final String tag,
            net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue queue,
            net.kyori.adventure.text.minimessage.Context context
    ) {
        final List<String> args = new LinkedList<>();
        while (queue.hasNext()) {
            args.add(queue.pop().value());
        }

        final var target = context.targetAsType(CommandSource.class);
        return new BasicContex<>(
                VelocitySynapse.get().asUser(target),
                tag,
                namespace.firstName(),
                args.toArray(String[]::new)
        );
    }

    public PluginContainer getPlugin() {
        return plugin;
    }
}
