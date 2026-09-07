package studio.mevera.synapse.command.impl;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import studio.mevera.imperat.BungeeCommandSource;
import studio.mevera.imperat.annotations.types.*;
import studio.mevera.synapse.BungeeSynapse;
import studio.mevera.synapse.util.SynapseHelpMessage;

@RootCommand("synapse")
@Permission("synapse.admin")
public class SynapseCommand {

    @Execute
    @SubCommand("help")
    public void sendHelp(BungeeCommandSource sender) {
        sender.reply(SynapseHelpMessage.getHelpMessage());
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(BungeeCommandSource sender, @Named("message") @Greedy String text) {
        BungeeSynapse synapse = BungeeSynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(BungeeCommandSource sender, @Named("target") ProxiedPlayer target, @Named("message") @Greedy String text) {
        BungeeSynapse synapse = BungeeSynapse.get();
        String parsed = synapse.translate(text, target);
        sender.reply(parsed);
    }

}
