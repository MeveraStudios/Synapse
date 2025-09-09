package studio.mevera.synapse.impl;

import studio.mevera.synapse.platform.UserBase;

import java.util.UUID;

public class TestUser extends UserBase {

    private final TestOrigin origin;

    public TestUser(final TestOrigin origin) {
        this.origin = origin;
    }

    @Override
    public String name() {
        return origin().name();
    }

    @Override
    public UUID uniqueId() {
        return origin().getUniqueId();
    }

    @Override
    public TestOrigin origin() {
        return this.origin;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isConsole() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

}
