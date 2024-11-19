package apple.utilities.logging;

import org.slf4j.event.Level;

@Deprecated
public record LogMessageImpl(String message, Level level, AppleLoggerName[] loggedNames) implements LogMessage {

}
