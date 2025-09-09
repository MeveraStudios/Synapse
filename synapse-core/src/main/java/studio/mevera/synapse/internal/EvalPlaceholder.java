package studio.mevera.synapse.internal;

import studio.mevera.synapse.placeholder.Placeholder;
import studio.mevera.synapse.placeholder.PlaceholderOptions;
import studio.mevera.synapse.placeholder.Context;
import studio.mevera.synapse.platform.User;

public class EvalPlaceholder<U extends User> implements Placeholder<U> {

    @Override
    public String name() {
        return "eval";
    }

    @Override
    public PlaceholderOptions options() {
        return PlaceholderOptions.DEFAULT;
    }

    @Override
    public String resolve(final Context<U> context) {
        return "";
    }

}

