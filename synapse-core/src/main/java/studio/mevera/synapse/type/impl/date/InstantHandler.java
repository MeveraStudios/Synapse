package studio.mevera.synapse.type.impl.date;

import studio.mevera.synapse.type.TypeHandler;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantHandler implements TypeHandler<Instant> {

    @Override
    public Type getType() {
        return Instant.class;
    }

    @Override
    public String handle(Instant value) {
        return DateTimeFormatter.ISO_INSTANT.format(value);
    }

}
