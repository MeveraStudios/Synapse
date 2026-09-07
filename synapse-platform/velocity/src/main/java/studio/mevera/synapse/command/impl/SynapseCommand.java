package studio.mevera.synapse.command.impl;

import com.velocitypowered.api.proxy.Player;
import studio.mevera.imperat.VelocityCommandSource;
import studio.mevera.imperat.annotations.types.*;
import studio.mevera.synapse.VelocitySynapse;
import studio.mevera.synapse.util.SynapseHelpMessage;

@RootCommand("synapse")
@Permission("synapse.admin")
public class SynapseCommand {

    @Execute
    @SubCommand("help")
    public void sendHelp(VelocityCommandSource sender) {
        sender.reply(SynapseHelpMessage.getHelpMessage());
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(VelocityCommandSource sender, @Named("message") @Greedy String text) {
        VelocitySynapse synapse = VelocitySynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(VelocityCommandSource sender, @Named("target") Player target, @Named("message") @Greedy String text) {
        VelocitySynapse synapse = VelocitySynapse.get();
        String parsed = synapse.translate(text, target);
        sender.reply(parsed);
    }

}
