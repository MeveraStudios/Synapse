package studio.mevera.synapse.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NeuronEntry {

    /**
     * The name of this neuron entry
     */
    String name();
    
    /**
     * Version of this neuron
     */
    String version() default "1.0.0";
    
    /**
     * Author of this neuron
     */
    String author() default "";
    
    /**
     * Description of what this neuron provides
     */
    String description() default "";

}