package studio.mevera.synapse.error.impl;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.error.ResolveResult;
import studio.mevera.synapse.error.ThrowableResolver;
import studio.mevera.synapse.platform.User;

public class PlaceholderException extends RuntimeException implements ThrowableResolver<PlaceholderException, User> {

    private final String replacement;

    public PlaceholderException(String replacement) {
        super(replacement);
        this.replacement = replacement;
    }

    @Override
    public ResolveResult resolve(PlaceholderException throwable, Context<User> context) {
        return ResolveResult.placeholder(this.replacement);
    }

}
