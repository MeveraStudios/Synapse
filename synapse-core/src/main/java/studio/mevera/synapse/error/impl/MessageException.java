package studio.mevera.synapse.error.impl;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.error.ResolveResult;
import studio.mevera.synapse.error.ThrowableResolver;
import studio.mevera.synapse.platform.User;

public class MessageException extends RuntimeException implements ThrowableResolver<MessageException, User> {

    private final String replacement;

    public MessageException(String replacement) {
        super(replacement);
        this.replacement = replacement;
    }

    @Override
    public ResolveResult resolve(MessageException throwable, Context<User> context) {
        return ResolveResult.message(this.replacement);
    }

}
