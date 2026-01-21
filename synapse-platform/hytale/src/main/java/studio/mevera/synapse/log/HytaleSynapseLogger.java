package studio.mevera.synapse.log;

import com.hypixel.hytale.logger.HytaleLogger;

public class HytaleSynapseLogger implements SynapseLogger {

    private final HytaleLogger logger;

    public HytaleSynapseLogger(HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.atInfo().log(message);
    }

    @Override
    public void warn(String message) {
        this.logger.atWarning().log(message);
    }

    @Override
    public void error(String message) {
        this.logger.atSevere().log(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.logger.atSevere().withCause(throwable).log(message);
    }

    @Override
    public void debug(String message) {
        this.logger.atFine().log(message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        this.logger.atFine().withCause(throwable).log(message);
    }

}
