package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ArrayHandler implements TypeHandler<Object[]> {

    private final TypeHandlerRegistry registry;

    public ArrayHandler(TypeHandlerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Type getType() {
        return Object[].class;
    }

    @Override
    public String handle(Object[] value) {
        if (value.length == 0) {
            return "";
        }
        
        return Arrays.stream(value)
                .map(registry::handle)
                .collect(Collectors.joining(", "));
    }

}