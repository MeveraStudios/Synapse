package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

public class CollectionHandler implements TypeHandler<Collection<?>> {

    private final TypeHandlerRegistry registry;

    public CollectionHandler(TypeHandlerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Type getType() {
        return Collection.class;
    }

    @Override
    public String handle(Collection<?> value) {
        if (value.isEmpty()) {
            return "";
        }

        return value.stream()
                .map(registry::handle)
                .collect(Collectors.joining(", "));
    }

}