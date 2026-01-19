package studio.mevera.synapse.command.impl;

import com.velocitypowered.api.proxy.Player;
import studio.mevera.imperat.VelocitySource;
import studio.mevera.imperat.annotations.Greedy;
import studio.mevera.imperat.annotations.Permission;
import studio.mevera.imperat.annotations.SubCommand;
import studio.mevera.imperat.annotations.Usage;
import studio.mevera.synapse.VelocitySynapse;
import studio.mevera.synapse.util.SynapseHelpMessage;

@Permission("synapse.admin")
public class SynapseCommand {

    @Usage
    @SubCommand("help")
    public void sendHelp(VelocitySource sender) {
        sender.reply(SynapseHelpMessage.getHelpMessage());
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(VelocitySource sender, @Greedy String text) {
        VelocitySynapse synapse = VelocitySynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(VelocitySource sender, Player target, @Greedy String text) {
        VelocitySynapse synapse = VelocitySynapse.get();
        String parsed = synapse.translate(text, target);
        sender.reply(parsed);
    }

}
