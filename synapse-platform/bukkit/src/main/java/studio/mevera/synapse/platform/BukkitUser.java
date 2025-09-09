package studio.mevera.synapse.platform;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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

    @Override
    public boolean isConsole() {
        return this.sender instanceof ConsoleCommandSender;
    }

    @Override
    public boolean isConnected() {
        return this.isConsole() || ((Player) this.sender).isOnline();
    }

    public Player asPlayer() {
        return (Player) this.sender;
    }

    public ConsoleCommandSender asConsole() {
        return (ConsoleCommandSender) this.sender;
    }

}
