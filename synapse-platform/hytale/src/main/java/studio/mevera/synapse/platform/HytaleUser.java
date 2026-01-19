package studio.mevera.synapse.platform;

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import studio.mevera.synapse.error.impl.IgnoreException;
import studio.mevera.synapse.error.impl.PlaceholderException;

import java.util.UUID;

public class HytaleUser extends UserBase {

    private final CommandSender sender;

    public HytaleUser(final CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String name() {
        return isPlayer() ? Universe.get().getPlayer(uniqueId()).getUsername() : "CONSOLE";
    }

    @Override
    public UUID uniqueId() {
        return this.sender.getUuid();
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

    public PlayerRef asPlayerRef() {
        return Universe.get().getPlayer(this.uniqueId());
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
        return this.isConsole() || ((Player) this.sender).getReference().isValid();
    }

}
