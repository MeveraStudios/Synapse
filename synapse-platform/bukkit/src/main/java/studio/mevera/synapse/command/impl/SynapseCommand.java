package studio.mevera.synapse.command.impl;

import org.bukkit.entity.Player;
import studio.mevera.imperat.BukkitSource;
import studio.mevera.imperat.annotations.*;
import studio.mevera.synapse.BukkitSynapse;
import studio.mevera.synapse.util.SynapseHelpMessage;

@Command("synapse")
@Permission("synapse.admin")
public class SynapseCommand {

    @Usage
    @SubCommand("help")
    public void sendHelp(BukkitSource sender) {
        sender.reply(SynapseHelpMessage.getHelpMessage());
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(BukkitSource sender, @Named("message") @Greedy String text) {
        BukkitSynapse synapse = BukkitSynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(BukkitSource sender, @Named("target") Player target, @Named("message") @Greedy String text) {
        BukkitSynapse synapse = BukkitSynapse.get();
        String parsed = synapse.translate(text, target);
        sender.reply(parsed);
    }

}
