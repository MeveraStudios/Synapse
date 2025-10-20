package studio.mevera.synapse.type.impl;

import studio.mevera.synapse.type.TypeHandler;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumHandler implements TypeHandler<Enum<?>> {
    
    @Override
    public Type getType() {
        return Enum.class;
    }
    
    @Override
    public String handle(Enum<?> value) {
        // Convert SCREAMING_SNAKE_CASE to Title Case
        return Arrays.stream(value.name().split("_"))
                .map(word -> word.charAt(0) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}