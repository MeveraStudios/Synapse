package studio.mevera.synapse.internal;

import studio.mevera.synapse.BungeePlugin;
import studio.mevera.synapse.platform.BungeeNeuron;
import studio.mevera.synapse.platform.BungeeUser;
import studio.mevera.synapse.platform.InternalNeuron;
import studio.mevera.synapse.platform.Namespace;

public class BungeeInternalNeuron extends BungeeNeuron implements InternalNeuron<BungeeUser> {

    public BungeeInternalNeuron() {
        super(BungeePlugin.getInstance(), Namespace.of("synapse", ""));
        this.registerInternalPlaceholders();
    }
}
