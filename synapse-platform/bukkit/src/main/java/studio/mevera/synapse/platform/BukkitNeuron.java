package studio.mevera.synapse.platform;

import lombok.Getter;
import net.kyori.adventure.identity.Identity;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import studio.mevera.synapse.BukkitSynapse;
import studio.mevera.synapse.hook.PAPIHook;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.placeholder.ContextBase;

import java.util.LinkedList;
import java.util.List;

@Getter
public class BukkitNeuron extends AdventureNeuronBase<BukkitUser> {

    private final Plugin plugin;
    private boolean papiHooked;

    public BukkitNeuron(final Plugin plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

    public void hookToPAPI() {
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && !papiHooked) {
            new PAPIHook(this).register();
            this.papiHooked = true;
        }
    }

    @Override
    protected Context<BukkitUser> toContext(
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
                ? plugin.getServer().getPlayer(uuid.get())
                : plugin.getServer().getConsoleSender();
        return new ContextBase<>(
                BukkitSynapse.get().asUser(target),
                tag,
                namespace.firstName().orElseThrow(),
                args.toArray(String[]::new)
        );
    }

}
