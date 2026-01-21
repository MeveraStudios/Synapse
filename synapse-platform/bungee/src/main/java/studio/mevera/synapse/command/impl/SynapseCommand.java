package studio.mevera.synapse.command.impl;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import studio.mevera.imperat.BungeeSource;
import studio.mevera.imperat.annotations.*;
import studio.mevera.synapse.BungeeSynapse;
import studio.mevera.synapse.util.SynapseHelpMessage;

@Command("synapse")
@Permission("synapse.admin")
public class SynapseCommand {

    @Usage
    @SubCommand("help")
    public void sendHelp(BungeeSource sender) {
        sender.reply(SynapseHelpMessage.getHelpMessage());
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(BungeeSource sender, @Named("message") @Greedy String text) {
        BungeeSynapse synapse = BungeeSynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(BungeeSource sender, @Named("target") ProxiedPlayer target, @Named("message") @Greedy String text) {
        BungeeSynapse synapse = BungeeSynapse.get();
        String parsed = synapse.translate(text, target);
        sender.reply(parsed);
    }

}
