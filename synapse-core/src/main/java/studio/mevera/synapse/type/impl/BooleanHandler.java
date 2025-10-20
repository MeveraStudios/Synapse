
package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.BaseTypeHandler;

public final class BooleanHandler extends BaseTypeHandler<Boolean> {

    @Override
    public String handle(Boolean input) {
        return String.valueOf(input);
    }

}
