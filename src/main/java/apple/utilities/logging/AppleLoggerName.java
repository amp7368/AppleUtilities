package apple.utilities.logging;

import org.slf4j.event.Level;

public interface AppleLoggerName {
    String getLoggerName();

    default Level[] getWatchLevels() {
        return new Level[0];
    }

    default void log(String message, Level level, AppleLoggerName[] loggerNames) {
        logInternal(new LogMessageImpl(message, level, loggerNames));
    }

    default void logInternal(LogMessage logMessage) {
    }
}
