package studio.mevera.synapse.platform;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import studio.mevera.synapse.error.impl.IgnoreException;
import studio.mevera.synapse.error.impl.PlaceholderException;

import java.util.UUID;

public class VelocityUser extends UserBase {

    private final CommandSource source;

    public VelocityUser(final CommandSource source) {
        this.source = source;
    }

    @Override
    public String name() {
        if (this.source instanceof Player player) {
            return player.getUsername();
        }
        return "Console";
    }

    @Override
    public UUID uniqueId() {
        if (this.source instanceof Player player) {
            return player.getUniqueId();
        }
        return User.CONSOLE_ID;
    }

    @Override
    public CommandSource origin() {
        return this.source;
    }

    @Override
    public boolean isPlayer() {
        return this.source instanceof Player;
    }

    public Player asPlayer() {
        return (Player) this.source;
    }

    public Player requirePlayer() {
        if (!this.isPlayer()) {
            throw new IgnoreException();
        }
        return this.asPlayer();
    }

    public Player requirePlayer(String message) {
        if (!this.isPlayer()) {
            throw new PlaceholderException(message);
        }
        return this.asPlayer();
    }

    @Override
    public boolean isConsole() {
        return this.source instanceof ConsoleCommandSource;
    }

    public ConsoleCommandSource asConsole() {
        return (ConsoleCommandSource) this.source;
    }

    public ConsoleCommandSource requireConsole() {
        if (!this.isConsole()) {
            throw new IgnoreException();
        }
        return this.asConsole();
    }

    public ConsoleCommandSource requireConsole(String message) {
        if (!this.isConsole()) {
            throw new PlaceholderException(message);
        }
        return this.asConsole();
    }

    @Override
    public boolean isConnected() {
        return this.isConsole() || ((Player) this.source).isActive();
    }

}
