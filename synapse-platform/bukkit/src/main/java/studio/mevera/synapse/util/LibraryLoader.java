package studio.mevera.synapse.util;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import com.alessiodp.libby.LibraryManager;
import com.alessiodp.libby.relocation.Relocation;
import studio.mevera.synapse.BukkitPlugin;

import java.util.function.Supplier;

public enum LibraryLoader {

    // ========================================================= //
    // Adventure and MiniMessage //
    MINI_MESSAGE(
            "net.kyori",
            "adventure-text-minimessage",
            "4.25.0",
            () -> !Utilities.findClass(String.join(".", "net", "kyori", "adventure", "text", "minimessage", "MiniMessage"))
    ),
    LEGACY_COMPONENT_SERIALIZER(
            "net.kyori",
            "adventure-text-serializer-legacy",
            "4.25.0",
            () -> !Utilities.findClass(String.join(".", "net", "kyori", "adventure", "text", "serializer", "legacy", "LegacyComponentSerializer"))
    );
    // ========================================================= //

    private static final LibraryManager MANAGER = new BukkitLibraryManager(BukkitPlugin.getInstance());

    static {
        MANAGER.addMavenCentral();
    }

    private final Library library;
    private final boolean shouldLoad;

    LibraryLoader(
            final String groupID,
            final String artifactID,
            final String version,
            final Supplier<Boolean> load,
            final Relocation... relocations
    ) {
        this(groupID, artifactID, version, null, load, relocations);
    }

    LibraryLoader(
            final String groupID,
            final String artifactID,
            final String version,
            final String repo,
            final Supplier<Boolean> load,
            final Relocation... relocations
    ) {
        final Library.Builder builder = Library.builder()
                .groupId(groupID)
                .artifactId(artifactID)
                .version(version);

        if (repo != null) {
            builder.repository(repo);
        }

        for (final Relocation relocation : relocations) {
            builder.relocate(relocation);
        }

        this.library = builder.resolveTransitiveDependencies(true).build();
        this.shouldLoad = load.get();
    }

    public static void loadLibraries() {
        for (final LibraryLoader value : values()) {
            if (value.shouldLoad) MANAGER.loadLibrary(value.library);
        }
    }

}
