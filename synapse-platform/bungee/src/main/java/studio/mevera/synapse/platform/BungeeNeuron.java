package studio.mevera.synapse.platform;

import net.kyori.adventure.identity.Identity;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import studio.mevera.synapse.BungeeSynapse;
import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.context.type.BasicContex;

import java.util.LinkedList;
import java.util.List;

public class BungeeNeuron extends AdventureNeuronBase<BungeeUser> {

    private final Plugin plugin;

    public BungeeNeuron(final Plugin plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

    @Override
    public void register() {
        BungeeSynapse.get().registerNeuron(this);
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

        var uuid = context.targetOrThrow().get(Identity.UUID);
        final CommandSender target = uuid.isPresent()
                ? plugin.getProxy().getPlayer(uuid.get())
                : plugin.getProxy().getConsole();
        return new BasicContex<>(
                BungeeSynapse.get().asUser(target),
                tag,
                namespace.firstName(),
                args.toArray(String[]::new)
        );
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
