package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.TypeHandler;
import studio.mevera.synapse.type.TypeHandlerRegistry;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class MapHandler implements TypeHandler<Map<?, ?>> {
    
    private final TypeHandlerRegistry registry;
    
    public MapHandler(TypeHandlerRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public Type getType() {
        return Map.class;
    }
    
    @Override
    public String handle(Map<?, ?> value) {
        if (value.isEmpty()) {
            return "";
        }
        
        return value.entrySet().stream()
                .map(entry -> registry.handle(entry.getKey()) + "=" + registry.handle(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}