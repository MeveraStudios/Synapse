package studio.mevera.synapse.internal;

import studio.mevera.synapse.VelocityPlugin;
import studio.mevera.synapse.platform.InternalNeuron;
import studio.mevera.synapse.platform.Namespace;
import studio.mevera.synapse.platform.VelocityNeuron;
import studio.mevera.synapse.platform.VelocityUser;

public class VelocityInternalNeuron extends VelocityNeuron implements InternalNeuron<VelocityUser> {

    public VelocityInternalNeuron() {
        super(VelocityPlugin.getInstance().getContainer(), Namespace.of("synapse", ""));
        this.register("player", context -> context.user().name());
        this.registerInternalPlaceholders();
    }
}
