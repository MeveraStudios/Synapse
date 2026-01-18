package studio.mevera.synapse.internal;

import studio.mevera.synapse.HytalePlugin;
import studio.mevera.synapse.platform.HytaleNeuron;
import studio.mevera.synapse.platform.HytaleUser;
import studio.mevera.synapse.platform.InternalNeuron;
import studio.mevera.synapse.platform.Namespace;

public class HytaleInternalNeuron extends HytaleNeuron implements InternalNeuron<HytaleUser> {

    public HytaleInternalNeuron() {
        super(HytalePlugin.getInstance(), Namespace.of("synapse", ""));
        this.registerInternalPlaceholders();
    }

}
