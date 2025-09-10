package studio.mevera.synapse.platform;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import studio.mevera.synapse.BungeeSynapse;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.ContextBase;

import java.util.LinkedList;
import java.util.List;

@Getter
public class BungeeNeuron extends AdventureNeuronBase<BungeeUser> {

    private final Plugin plugin;

    public BungeeNeuron(final Plugin plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

    @Override
    protected Context<BungeeUser> toContext(
            final String tag,
            net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue queue,
            net.kyori.adventure.text.minimessage.Context context
    ) {
        final List<String> args = new LinkedList<>();
        while (queue.hasNext()) {
            args.add(queue.pop().value());
        }

        final CommandSender target = (CommandSender) context.target();
        return new ContextBase<>(
                BungeeSynapse.get().asUser(target),
                tag,
                namespace.firstName().orElseThrow(),
                args.toArray(String[]::new)
        );
    }
}
