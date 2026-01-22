package studio.mevera.synapse.loader;

import studio.mevera.synapse.SynapseBase;
import studio.mevera.synapse.annotation.NeuronEntry;
import studio.mevera.synapse.platform.Neuron;
import studio.mevera.synapse.platform.User;

import java.io.IOException;
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
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                // Only process .class files
                if (!name.endsWith(".class")) {
                    continue;
                }

                // Convert path to class name
                String className = name.replace('/', '.')
                        .substring(0, name.length() - 6);

                try {
                    Class<?> clazz = classLoader.loadClass(className);

                    // Only load classes marked as neurons
                    if (!clazz.isAnnotationPresent(NeuronEntry.class)) {
                        continue;
                    }

                    // It has @NeuronEntry, but doesn't implement Neuron -> warn and skip
                    if (!Neuron.class.isAssignableFrom(clazz)) {
                        this.synapse.getLogger().warn("Class " + className + " has @NeuronEntry but doesn't implement Neuron (Skipping)");
                        continue;
                    }

                    NeuronEntry annotation = clazz.getAnnotation(NeuronEntry.class);

                    // Instantiate neuron
                    N neuron = this.instantiateNeuron(clazz, className);
                    neurons.add(new LoadedNeuron<>(neuron, annotation, classLoader, jarPath));
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    // Usually means missing dependency inside the jar
                    this.synapse.getLogger().debug("Skipping class due to missing dependency: " + className, e);

                } catch (Exception e) {
                    this.synapse.getLogger().error("Failed to instantiate neuron class: " + className, e);
                }
            }
        } catch (IOException e) {
            this.synapse.getLogger().error("Failed to open jar: " + jarPath, e);
            throw e;
        }

        return neurons;
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
            NeuronEntry annotation,
            ClassLoader classLoader,
            Path jarPath
    ) {
        public String getName() {
            return annotation.name();
        }

        public String getVersion() {
            return annotation.version();
        }

        public String getAuthor() {
            return annotation.author();
        }

        public String getDescription() {
            return annotation.description();
        }
    }

}
