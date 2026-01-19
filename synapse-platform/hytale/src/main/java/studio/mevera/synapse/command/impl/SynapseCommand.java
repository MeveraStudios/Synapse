package studio.mevera.synapse.command.impl;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import studio.mevera.imperat.HytaleSource;
import studio.mevera.imperat.annotations.*;
import studio.mevera.synapse.HytaleSynapse;

@Command("synapse")
@Permission("synapse.admin")
public class SynapseCommand {

    private final Message helpMessage = this.getHelpMessage();

    @Usage
    @SubCommand("help")
    public void sendHelp(HytaleSource sender) {
        sender.reply(this.helpMessage);
    }

    @SubCommand("selfparse")
    @Permission("synapse.admin.selfparse")
    public void selfParse(HytaleSource sender, @Greedy String text) {
        HytaleSynapse synapse = HytaleSynapse.get();
        String parsed = synapse.translate(text, sender.origin());
        sender.reply(parsed);
    }

    @SubCommand("parse")
    @Permission("synapse.admin.parseother")
    public void parse(HytaleSource sender, PlayerRef target, @Greedy String text) {
        HytaleSynapse synapse = HytaleSynapse.get();
        World world = Universe.get().getWorld(target.getUuid());
        if (world == null) {
            sender.reply("Could not find the target player's world.");
            return;
        }
        var refrence = target.getReference();
        if (refrence == null || !refrence.isValid()) {
            sender.reply("Could not find the target player.");
            return;
        }
        world.execute(() -> {
            Player player = target.getReference().getStore().getComponent(refrence, Player.getComponentType());
            String parsed = synapse.translate(text, player);
            sender.reply(parsed);
        });
    }

    private Message getHelpMessage() {
        return Message.raw("Synapse")
                .color("#55FFFF")
                .bold(true)
                .insert(Message.raw(" - Commands").color("#AAAAAA"))
                .insert("\n\n")
                .insert(Message.raw("/synapse help").color("#FFFFFF"))
                .insert(Message.raw(" - Show this help message").color("#AAAAAA"))
                .insert("\n")
                .insert(Message.raw("/synapse selfparse ").color("#FFFFFF"))
                .insert(Message.raw("<text>").color("#FFFF55"))
                .insert(Message.raw(" - Parse text as yourself").color("#AAAAAA"))
                .insert("\n")
                .insert(Message.raw("/synapse parse ").color("#FFFFFF"))
                .insert(Message.raw("<player> ").color("#FFFF55"))
                .insert(Message.raw("<text>").color("#FFFF55"))
                .insert(Message.raw(" - Parse text as another player").color("#AAAAAA"));
    }

}
