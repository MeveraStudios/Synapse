package studio.mevera.synapse.internal;

import studio.mevera.synapse.BukkitPlugin;
import studio.mevera.synapse.platform.BukkitNeuron;
import studio.mevera.synapse.platform.BukkitUser;
import studio.mevera.synapse.platform.InternalNeuron;
import studio.mevera.synapse.platform.Namespace;

public class BukkitInternalNeuron extends BukkitNeuron implements InternalNeuron<BukkitUser> {

    public BukkitInternalNeuron() {
        super(BukkitPlugin.getInstance(), Namespace.of("synapse", ""));
        this.registerInternalPlaceholders();
    }

}
