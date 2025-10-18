package studio.mevera.synapse.platform;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a namespace for placeholders.
 * A namespace is a set of strings that can be used to categorize or group placeholders.
 */
public final class Namespace {

    /**
     * Creates a Namespace from a variable number of names.
     *
     * @param names the names to include in the namespace
     * @return a new Namespace instance
     * @throws IllegalArgumentException if no names are provided
     */
    public static Namespace of(final String... names) {
        return new Namespace(Set.of(names));
    }

    /**
     * Creates a Namespace from a collection of names.
     *
     * @param names the collection of names to include in the namespace
     * @return a new Namespace instance
     * @throws IllegalArgumentException if the collection is empty
     */
    public static Namespace of(final Collection<String> names) {
        return new Namespace(names);
    }

    private final Set<String> names;

    private Namespace(final Collection<String> names) {
        this.names = names.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (this.names.isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be empty");
        }
    }

    public String firstName() {
        return this.names.stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Namespace cannot be empty"));
    }

    public Set<String> getNames() {
        return names;
    }

    public boolean isEmpty() {
        return this.names.isEmpty();
    }

}
