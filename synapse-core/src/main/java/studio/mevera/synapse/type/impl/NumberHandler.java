package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.BaseTypeHandler;

public class NumberHandler extends BaseTypeHandler<Number> {

    @Override
    public String handle(Number input) {
        return String.valueOf(input);
    }

}
