package studio.mevera.synapse.platform;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

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

    @Override
    public boolean isConsole() {
        return this.source instanceof ConsoleCommandSource;
    }

    @Override
    public boolean isConnected() {
        return this.isConsole() || ((Player) this.source).isActive();
    }

    public Player asPlayer() {
        return (Player) this.source;
    }

    public ConsoleCommandSource asConsole() {
        return (ConsoleCommandSource) this.source;
    }
}
