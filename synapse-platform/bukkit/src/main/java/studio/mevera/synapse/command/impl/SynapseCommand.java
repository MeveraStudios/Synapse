package studio.mevera.synapse.command.impl;

import org.bukkit.entity.Player;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.*;
import studio.mevera.synapse.BukkitSynapse;
import studio.mevera.synapse.util.SynapseHelpMessage;

@RootCommand("synapse")
@Permission("synapse.admin")
public class SynapseCommand {

    @Execute
    @SubCommand("help")
    public void sendHelp(BukkitCommandSource sender) {
        sender.reply(SynapseHelpMessage.getHelpMessage());
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(BukkitCommandSource sender, @Named("message") @Greedy String text) {
        BukkitSynapse synapse = BukkitSynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(BukkitCommandSource sender, @Named("target") Player target, @Named("message") @Greedy String text) {
        BukkitSynapse synapse = BukkitSynapse.get();
        String parsed = synapse.translate(text, target);
        sender.reply(parsed);
    }

}
