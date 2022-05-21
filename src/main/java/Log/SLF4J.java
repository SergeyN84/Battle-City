package Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4J {
    private static Logger logger = LoggerFactory.getLogger(SLF4J.class);

    public static final Logger getLogger() {return logger;};

    public static void logInfo(String text) {
        logger.info(text);
    }

    public static void logDebug(String text) {
        logger.debug(text);
    }

    public static void logError(String text) {
        logger.error(text);
    }
}
