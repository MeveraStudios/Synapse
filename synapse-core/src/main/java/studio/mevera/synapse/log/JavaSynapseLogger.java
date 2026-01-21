package studio.mevera.synapse.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaSynapseLogger implements SynapseLogger {

    private final Logger logger;

    public JavaSynapseLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    @Override
    public void debug(String message) {
        logger.fine(message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logger.log(Level.FINE, message, throwable);
    }

}