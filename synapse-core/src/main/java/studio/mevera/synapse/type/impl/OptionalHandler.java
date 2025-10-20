package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.BaseTypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.util.Optional;

public final class OptionalHandler extends BaseTypeHandler<Optional<?>> {

    private final TypeHandlerRegistry typeHandlerRegistry;

    public OptionalHandler(TypeHandlerRegistry typeHandlerRegistry) {
        super(Optional.class);
        this.typeHandlerRegistry = typeHandlerRegistry;
    }

    @Override
    public String handle(Optional<?> input) {
        return typeHandlerRegistry.handle(input.orElse(null));
    }

}
