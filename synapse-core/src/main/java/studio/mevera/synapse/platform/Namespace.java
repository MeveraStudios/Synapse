package studio.mevera.synapse.platform;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a namespace for placeholders.
 * A namespace is a set of strings that can be used to categorize or group placeholders.
 */
public final class Namespace {

    public static Namespace of(final String... namespaces) {
        return new Namespace(Set.of(namespaces));
    }

    public static Namespace of(final Collection<String> namespaces) {
        return new Namespace(namespaces);
    }

    private final Set<String> namespaces;

    private Namespace(final Collection<String> namespaces) {
        this.namespaces = namespaces.stream().
                map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public Set<String> getNamespaces() {
        return namespaces;
    }

}
