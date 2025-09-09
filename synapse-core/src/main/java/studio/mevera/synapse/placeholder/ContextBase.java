package studio.mevera.synapse.placeholder;

import studio.mevera.synapse.platform.User;

public record ContextBase<U extends User>(U user, String tag, String namespace, String[] arguments) implements Context<U> {}