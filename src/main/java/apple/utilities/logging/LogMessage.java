package apple.utilities.logging;

import org.slf4j.event.Level;

public interface LogMessage {
    String message();

    Level level();

    AppleLoggerName[] loggedNames();
}
