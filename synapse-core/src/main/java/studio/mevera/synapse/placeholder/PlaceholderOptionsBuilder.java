package studio.mevera.synapse.placeholder;

public interface PlaceholderOptionsBuilder<O extends PlaceholderOptions> {

    /**
     * Builds the placeholder options.
     *
     * @return The built placeholder options.
     */
    O build();
}
