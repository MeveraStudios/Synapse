package studio.mevera.synapse.loader;

import studio.mevera.synapse.SynapseBase;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class NeuronLoader<O, U extends User, N extends Neuron<U>> {

    private final SynapseBase<O, U, N> synapse;
    private final ClassLoader parentClassLoader;
    private final Gson gson = new GsonBuilder().create();

    public NeuronLoader(SynapseBase<O, U, N> synapse) {
        this(synapse, NeuronLoader.class.getClassLoader());
    }

    public NeuronLoader(SynapseBase<O, U, N> synapse, ClassLoader parentClassLoader) {
        this.synapse = synapse;
        this.parentClassLoader = parentClassLoader;
    }

    /**
     * Loads all neurons from JAR files in the specified directory.
     *
     * @param directory the directory to scan for JAR files
     * @return a collection of loaded neurons (may be empty)
     * @throws IOException if the directory cannot be accessed or created
     */
    public Collection<LoadedNeuron<N>> loadFromDirectory(Path directory) throws IOException {
        if (!Files.exists(directory) || directory.getNameCount() == 0) {
            this.synapse.getLogger().warn("Neuron directory doesn't exist (or invalid path). Creating it: " + directory);
            Files.createDirectories(directory);
            return Collections.emptyList();
        }

        this.synapse.getLogger().info("Scanning neuron directory: " + directory);

        final List<LoadedNeuron<N>> neurons = new ArrayList<>();

        try (final Stream<Path> paths = Files.walk(directory, 1)) {
            paths.filter(p -> p.toString().endsWith(".jar"))
                    .forEach(jarPath -> {
                        this.synapse.getLogger().debug("Found neuron jar: " + jarPath.getFileName());

                        try {
                            List<LoadedNeuron<N>> loaded = loadNeuronsFromJar(jarPath);
                            neurons.addAll(loaded);

                            if (!loaded.isEmpty()) {
                                this.synapse.getLogger().info("Found " + loaded.size() + " neuron(s) from: " + jarPath.getFileName());
                            } else {
                                this.synapse.getLogger().debug("No neurons found inside: " + jarPath.getFileName());
                            }

                        } catch (Exception e) {
                            this.synapse.getLogger().error("Failed to read neurons from jar: " + jarPath, e);
                        }
                    });
        }

        this.synapse.getLogger().info("Neuron scan complete. Total loaded neurons: " + neurons.size());
        return neurons;
    }

    /**
     * Loads neurons from a single JAR file.
     *
     * @param jarPath the path to the JAR file
     * @return a list of loaded neurons from the jar
     * @throws Exception if jar reading fails unexpectedly
     */
    private List<LoadedNeuron<N>> loadNeuronsFromJar(Path jarPath) throws Exception {
        URL jarUrl = jarPath.toUri().toURL();

        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{jarUrl},
                parentClassLoader
        );

        List<LoadedNeuron<N>> neurons = new ArrayList<>();

        try (JarFile jar = new JarFile(jarPath.toFile())) {
            Optional<NeuronManifest> manifestOptional = readManifest(jarPath.getFileName().toString(), jar);

            if (manifestOptional.isPresent()) {
                loadFromManifest(jarPath, classLoader, neurons, manifestOptional.get());
            } else {
                this.synapse.getLogger().info("Skipping jar " + jarPath.getFileName() + " (no neuron manifest found)");
            }
        } catch (IOException e) {
            this.synapse.getLogger().error("Failed to open jar: " + jarPath, e);
            throw e;
        }

        return neurons;
    }

    /**
     * Attempts to find & read a neuron manifest from the given jar file.
     *
     * @param jarName the name of the jar (for logging)
     * @param jar     the JarFile to read from
     * @return an Optional containing the NeuronManifest if found and parsed successfully, or empty if not found or failed
     */
    private Optional<NeuronManifest> readManifest(String jarName, JarFile jar) {
        final List<String> candidates = List.of("neuron.json", "manifest.json", "synapse-neuron.json");
        for (String candidate : candidates) {
            JarEntry entry = jar.getJarEntry(candidate);
            if (entry == null) {
                continue;
            }
            try (InputStream is = jar.getInputStream(entry); InputStreamReader reader = new InputStreamReader(is)) {
                NeuronManifest manifest = gson.fromJson(reader, NeuronManifest.class);
                if (manifest != null) {
                    return Optional.of(manifest.normalize());
                }
            } catch (Exception ignored) {
                this.synapse.getLogger().warn("Failed to parse neuron manifest (" + candidate + ") in " + jarName);
            }
        }
        return Optional.empty();
    }

    /**
     * Loads a neuron from the given manifest and adds it to the list of loaded neurons.
     *
     * @param jarPath    the path to the jar file (for logging)
     * @param classLoader the class loader to use for loading the neuron class
     * @param neurons    the list to add the loaded neuron to
     * @param manifest   the manifest containing neuron metadata and main class info
     */
    private void loadFromManifest(
            Path jarPath,
            URLClassLoader classLoader,
            List<LoadedNeuron<N>> neurons,
            NeuronManifest manifest
    ) {
        String platformName = synapse.platform().name().toLowerCase(Locale.ROOT);
        NeuronManifest.PlatformEntry platformEntry = manifest.platforms().get(platformName);

        if (platformEntry == null) {
            this.synapse.getLogger().info("Skipping jar " + jarPath.getFileName() + " because manifest has no entry for platform: " + platformName);
            return;
        }

        String mainClassName = platformEntry.main();
        if (mainClassName == null || mainClassName.isBlank()) {
            this.synapse.getLogger().warn("Manifest in " + jarPath.getFileName() + " lacks main class for platform " + platformName);
            return;
        }

        try {
            Class<?> clazz = classLoader.loadClass(mainClassName);
            if (!Neuron.class.isAssignableFrom(clazz)) {
                this.synapse.getLogger().warn("Main class " + mainClassName + " does not implement Neuron. Skipping jar " + jarPath.getFileName());
                return;
            }

            N neuron = this.instantiateNeuron(clazz, mainClassName);
            if (neuron == null) {
                this.synapse.getLogger().error("Failed to instantiate neuron main class " + mainClassName + " for jar " + jarPath.getFileName());
                return;
            }

            neurons.add(new LoadedNeuron<>(
                    neuron,
                    manifest.name(),
                    manifest.version(),
                    String.join(", ", manifest.authors()),
                    manifest.description(),
                    classLoader,
                    jarPath
            ));
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            this.synapse.getLogger().error("Main class " + mainClassName + " not found in jar " + jarPath.getFileName(), e);
        } catch (Exception e) {
            this.synapse.getLogger().error("Failed to load neuron from manifest in jar " + jarPath.getFileName(), e);
        }
    }

    /**
     * Attempts to instantiate a neuron class using registered dependencies.
     *
     * @param clazz     the neuron class to instantiate
     * @param className the class name (for logging)
     * @return the instantiated neuron, or null if instantiation failed
     * @throws Exception if instantiation fails unexpectedly
     */
    @SuppressWarnings("unchecked")
    private N instantiateNeuron(Class<?> clazz, String className) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Arrays.sort(constructors, (a, b) -> Integer.compare(b.getParameterCount(), a.getParameterCount()));

        for (Constructor<?> constructor : constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] args = new Object[paramTypes.length];

            boolean canInstantiate = true;

            for (int i = 0; i < paramTypes.length; i++) {
                Optional<?> dependency = this.synapse.getDependencyRegistry().get(paramTypes[i]);

                if (dependency.isPresent()) {
                    args[i] = dependency.get();
                } else {
                    canInstantiate = false;
                    break;
                }
            }

            if (canInstantiate) {
                constructor.setAccessible(true);
                this.synapse.getLogger().debug("Instantiating " + className + " with " + paramTypes.length + " dependencies");
                return (N) constructor.newInstance(args);
            }
        }

        this.synapse.getLogger().error("No suitable constructor found for " + className +
                ". Required dependencies not registered.");
        return null;
    }

    /**
     * Container for a loaded neuron and its metadata.
     */
    public record LoadedNeuron<N extends Neuron<?>>(
            N neuron,
            String name,
            String version,
            String author,
            String description,
            ClassLoader classLoader,
            Path jarPath
    ) {}

}
