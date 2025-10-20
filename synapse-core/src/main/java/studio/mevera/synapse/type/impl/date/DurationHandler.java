package studio.mevera.synapse.type.impl.date;

import studio.mevera.synapse.type.TypeHandler;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationHandler implements TypeHandler<Duration> {

    @Override
    public Type getType() {
        return Duration.class;
    }

    @Override
    public String handle(Duration value) {
        long hours = value.toHours();
        long minutes = value.toMinutesPart();
        long seconds = value.toSecondsPart();
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

}