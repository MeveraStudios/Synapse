package studio.mevera.synapse.util;

import java.util.Arrays;

public record ArrayKey(String[] args) {

    public static ArrayKey of(String... args) {
        return new ArrayKey(args);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ArrayKey(String[] args1) && Arrays.equals(args, args1));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(args);
    }

}
