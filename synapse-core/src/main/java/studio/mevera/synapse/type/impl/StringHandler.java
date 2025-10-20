package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.BaseTypeHandler;

public class StringHandler extends BaseTypeHandler<String> {

    @Override
    public String handle(String input) {
        return input;
    }

}
