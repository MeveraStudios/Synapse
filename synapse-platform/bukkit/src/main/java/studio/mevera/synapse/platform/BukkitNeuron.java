package studio.mevera.synapse.platform;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class BukkitNeuron extends NeuronBase<BukkitUser> {

    private final Plugin plugin;

    public BukkitNeuron(final Plugin plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

}
