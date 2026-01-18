package studio.mevera.synapse.platform;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import studio.mevera.synapse.HytaleSynapse;

public class HytaleNeuron extends NeuronBase<HytaleUser> {

    private final JavaPlugin plugin;

    public HytaleNeuron(final JavaPlugin plugin, final Namespace namespace) {
        super(namespace);
        this.plugin = plugin;
    }

    @Override
    public void register() {
        HytaleSynapse.get().registerNeuron(this);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
