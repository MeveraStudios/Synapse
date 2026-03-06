package studio.mevera.synapse.loader;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents neuron metadata declared via neuron.json manifest.
 */
public record NeuronManifest(
        int schema,
        String id,
        String name,
        String version,
        int apiVersion,
        Map<String, PlatformEntry> platforms,
        String description,
        List<String> authors
) {
    public NeuronManifest normalize() {
        // Normalize platform keys to lower-case for consistent lookups
        Map<String, PlatformEntry> normalizedPlatforms = platforms == null
                ? Collections.emptyMap()
                : platforms.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        e -> e.getKey().toLowerCase(Locale.ROOT),
                        Map.Entry::getValue
                ));

        return new NeuronManifest(
                schema,
                id,
                name,
                version,
                apiVersion,
                normalizedPlatforms,
                description,
                authors == null ? List.of() : List.copyOf(authors)
        );
    }

    public record PlatformEntry(
            String main,
            String platformVersion
    ) {}
}
