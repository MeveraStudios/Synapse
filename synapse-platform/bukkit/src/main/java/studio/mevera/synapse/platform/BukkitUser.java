package studio.mevera.synapse.platform;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import studio.mevera.synapse.error.impl.IgnoreException;
import studio.mevera.synapse.error.impl.PlaceholderException;

import java.util.UUID;

public class BukkitUser extends UserBase {

    private final CommandSender sender;

    /**
     * Constructs a new BukkitUser with the given CommandSender.
     *
     * @param sender the CommandSender representing the user
     */
    public BukkitUser(final CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String name() {
        return this.sender.getName();
    }

    @Override
    public UUID uniqueId() {
        if (this.sender instanceof Player player) {
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
        return this.sender instanceof Player;
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

    public Player asPlayer() {
        return (Player) this.sender;
    }

    @Override
    public boolean isConsole() {
        return this.sender instanceof ConsoleCommandSender;
    }

    public ConsoleCommandSender asConsole() {
        return (ConsoleCommandSender) this.sender;
    }

    public ConsoleCommandSender requireConsole() {
        if (!this.isConsole()) {
            throw new IgnoreException();
        }
        return this.asConsole();
    }

    public ConsoleCommandSender requireConsole(String message) {
        if (!this.isConsole()) {
            throw new PlaceholderException(message);
        }
        return this.asConsole();
    }

    @Override
    public boolean isConnected() {
        return this.isConsole() || ((Player) this.sender).isOnline();
    }

}
