package apple.utilities.logging;

import org.slf4j.event.Level;

import java.util.Objects;

public final class LogMessageImpl implements LogMessage {
    private final String message;
    private final Level level;
    private final AppleLoggerName[] loggedNames;

    public LogMessageImpl(String message, Level level, AppleLoggerName[] loggedNames) {
        this.message = message;
        this.level = level;
        this.loggedNames = loggedNames;
    }

    public String message() {
        return message;
    }

    public Level level() {
        return level;
    }

    public AppleLoggerName[] loggedNames() {
        return loggedNames;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LogMessageImpl) obj;
        return Objects.equals(this.message, that.message) &&
                Objects.equals(this.level, that.level) &&
                Objects.equals(this.loggedNames, that.loggedNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, level, loggedNames);
    }

    @Override
    public String toString() {
        return "LogMessageImpl[" +
                "message=" + message + ", " +
                "level=" + level + ", " +
                "loggedNames=" + loggedNames + ']';
    }

}
