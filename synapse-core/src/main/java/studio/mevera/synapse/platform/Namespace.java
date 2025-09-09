package studio.mevera.synapse.platform;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a namespace for placeholders.
 * A namespace is a set of strings that can be used to categorize or group placeholders.
 */
public final class Namespace {

    public static Namespace of(final String... names) {
        return new Namespace(Set.of(names));
    }

    public static Namespace of(final Collection<String> names) {
        return new Namespace(names);
    }

    private final Set<String> names;

    private Namespace(final Collection<String> names) {
        this.names = names.stream().
                map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public Optional<String> firstName() {
        return this.names.stream().findFirst();
    }

    public Set<String> getNames() {
        return names;
    }

    public boolean isEmpty() {
        return this.names.isEmpty();
    }

}
