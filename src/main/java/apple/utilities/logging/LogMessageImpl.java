package apple.utilities.logging;

import org.slf4j.event.Level;

public record LogMessageImpl(String message, Level level, AppleLoggerName[] loggedNames) implements LogMessage {
}
