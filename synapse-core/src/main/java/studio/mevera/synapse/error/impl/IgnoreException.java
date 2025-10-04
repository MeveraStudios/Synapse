package studio.mevera.synapse.error.impl;

import studio.mevera.synapse.context.Context;
import studio.mevera.synapse.error.ResolveResult;
import studio.mevera.synapse.error.ThrowableResolver;
import studio.mevera.synapse.platform.User;

public class IgnoreException extends RuntimeException implements ThrowableResolver<IgnoreException, User> {

    @Override
    public ResolveResult resolve(IgnoreException throwable, Context<User> context) {
        return ResolveResult.ignore();
    }

}
