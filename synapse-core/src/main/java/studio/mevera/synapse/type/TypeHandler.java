package studio.mevera.synapse.type;

import java.lang.reflect.Type;

public interface TypeHandler<T> {

    /**
     * Gets the type this handler is responsible for.
     *
     * @return The type this handler handles.
     */
    Type getType();

    /**
     * Handles the input of type T and returns a String representation.
     *
     * @param input The input to handle.
     * @return The String representation of the input.
     */
    String handle(T input);

}
