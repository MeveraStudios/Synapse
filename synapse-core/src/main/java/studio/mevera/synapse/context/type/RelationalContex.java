package studio.mevera.synapse.context.type;

import studio.mevera.synapse.platform.User;

public class RelationalContex<U extends User> extends BasicContex<U> {

    private final U other;

    public RelationalContex(U user, U other, String tag, String namespace, String... arguments) {
        super(user, tag, namespace, arguments);
        this.other = other;
    }

    public U other() {
        return this.other;
    }

    @Override
    public boolean isRelational() {
        return true;
    }
}