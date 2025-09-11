package studio.mevera.synapse.context.type;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.platform.User;

public class BasicContex<U extends User> implements Context<U> {

    private final U user;
    private final String tag;
    private final String namespace;
    private final String[] arguments;

    public BasicContex(U user, String tag, String namespace, String[] arguments) {
        this.user = user;
        this.tag = tag;
        this.namespace = namespace;
        this.arguments = arguments;
    }

    @Override
    public U user() {
        return this.user;
    }

    @Override
    public String tag() {
        return this.tag;
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public String[] arguments() {
        return this.arguments;
    }
}