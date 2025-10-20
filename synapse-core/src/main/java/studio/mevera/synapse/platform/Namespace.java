package studio.mevera.synapse.platform;

import java.util.*;
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

    private final transient String firstNameCache;
    private final transient String shortestNameCache;

    private Namespace(final Collection<String> names) {
        this.names = names.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (this.names.isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be empty");
        }

        this.firstNameCache = this.findFirstName();
        this.shortestNameCache = this.findShortestName();
    }

    /**
     * Gets the first name in the namespace.
     *
     * @return the first name
     */
    public String firstName() {
        return this.firstNameCache;
    }

    /**
     * Gets the shortest name in the namespace.
     *
     * @return the shortest name
     */
    public String shortestName() {
        return this.shortestNameCache;
    }

    /**
     * Gets the set of names in the namespace.
     *
     * @return the set of names
     */
    public Set<String> getNames() {
        return names;
    }

    /**
     * Checks if the namespace is empty.
     *
     * @return true if the namespace is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.names.isEmpty();
    }

    private String findFirstName() {
        return this.names.stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Namespace cannot be empty"));
    }

    private String findShortestName() {
        return this.names.stream().min(Comparator.comparingInt(String::length)).orElseThrow(() -> new IllegalArgumentException("Namespace cannot be empty"));
    }

}
