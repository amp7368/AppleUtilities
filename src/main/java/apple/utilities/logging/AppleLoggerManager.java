package apple.utilities.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class AppleLoggerManager {
    private final HashMap<AppleLoggerName, Logger> loggers = new HashMap<>();
    private final HashMap<Level, List<AppleLoggerName>> catchAll = new HashMap<>();

    private final Logger defaultLogger;


    public AppleLoggerManager(AppleLoggerName[] loggerNames, Logger defaultLogger) {
        this.defaultLogger = defaultLogger;
        for (AppleLoggerName appleLoggerName : loggerNames) {
            loggers.put(appleLoggerName, LoggerFactory.getLogger(appleLoggerName.getLoggerName()));
            for (Level watchLevel : appleLoggerName.getWatchLevels()) {
                catchAll.computeIfAbsent(watchLevel, (k) -> new ArrayList<>()).add(appleLoggerName);
            }
        }
    }

    public void log(String message, Level level, AppleLoggerName... loggerNames) {
        BiConsumer<Logger, String> action;
        action = switch (level) {
            case TRACE -> Logger::trace;
            case DEBUG -> Logger::debug;
            case INFO -> Logger::info;
            case WARN -> Logger::warn;
            default -> Logger::error;
        };
        for (AppleLoggerName loggerName : loggerNames) {
            action.accept(loggers.computeIfAbsent(loggerName, (k) -> defaultLogger), message);
        }
        List<AppleLoggerName> catching = catchAll.get(level);
        if (catching != null) {
            for (AppleLoggerName loggerName : catching) {
                loggerName.log(message, level, loggerNames);
            }
        }
    }
}
