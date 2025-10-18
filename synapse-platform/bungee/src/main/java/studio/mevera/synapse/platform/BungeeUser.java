package studio.mevera.synapse.platform;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import studio.mevera.synapse.error.impl.IgnoreException;
import studio.mevera.synapse.error.impl.PlaceholderException;

import java.util.UUID;

public class BungeeUser extends UserBase {

    private final CommandSender sender;

    public BungeeUser(final CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String name() {
        return this.sender.getName();
    }

    @Override
    public UUID uniqueId() {
        if (this.sender instanceof ProxiedPlayer player) {
            return player.getUniqueId();
        }
        return User.CONSOLE_ID;
    }

    @Override
    public CommandSender origin() {
        return this.sender;
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof ProxiedPlayer;
    }

    public ProxiedPlayer requirePlayer() {
        if (!this.isPlayer()) {
            throw new IgnoreException();
        }
        return this.asPlayer();
    }

    public ProxiedPlayer requirePlayer(String message) {
        if (!this.isPlayer()) {
            throw new PlaceholderException(message);
        }
        return this.asPlayer();
    }

    public ProxiedPlayer asPlayer() {
        return (ProxiedPlayer) this.sender;
    }

    @Override
    public boolean isConsole() {
        return !this.isPlayer();
    }

    public CommandSender asConsole() {
        return this.sender;
    }

    public CommandSender requireConsole() {
        if (!this.isConsole()) {
            throw new IgnoreException();
        }
        return this.asConsole();
    }

    public CommandSender requireConsole(String message) {
        if (!this.isConsole()) {
            throw new PlaceholderException(message);
        }
        return this.asConsole();
    }

    @Override
    public boolean isConnected() {
        return this.isConsole() || ((ProxiedPlayer) this.sender).isConnected();
    }

}
