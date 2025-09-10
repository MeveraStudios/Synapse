package studio.mevera.synapse.platform;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeeNeuron extends NeuronBase<BungeeUser> {

    private final Plugin plugin;

    public BungeeNeuron(final Plugin plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

}
