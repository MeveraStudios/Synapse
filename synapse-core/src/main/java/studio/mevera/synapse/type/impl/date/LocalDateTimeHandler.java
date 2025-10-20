package studio.mevera.synapse.type.impl.date;

import studio.mevera.synapse.type.TypeHandler;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public Type getType() {
        return LocalDateTime.class;
    }

    @Override
    public String handle(LocalDateTime value) {
        return value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}