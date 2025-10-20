package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.lang.reflect.Type;
import java.util.function.Supplier;

public class SupplierHandler implements TypeHandler<Supplier<?>> {

    private final TypeHandlerRegistry registry;

    public SupplierHandler(TypeHandlerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Type getType() {
        return Supplier.class;
    }

    @Override
    public String handle(Supplier<?> value) {
        Object result = value.get();
        return registry.handle(result);
    }

}