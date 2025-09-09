package studio.mevera.synapse.impl;

import java.util.UUID;

public class TestOrigin {

    private final String name;
    private final UUID uniqueId;

    public TestOrigin(String name) {
        this.name = name;
        this.uniqueId = UUID.nameUUIDFromBytes(name.getBytes());
    }

    public String name() {
        return name;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TestOrigin other) &&
                this.name.equals(other.name) &&
                this.uniqueId.equals(other.uniqueId);
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ uniqueId.hashCode();
    }
}
