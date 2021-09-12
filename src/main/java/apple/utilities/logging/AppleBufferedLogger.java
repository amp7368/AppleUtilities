package apple.utilities.logging;

import java.util.ArrayList;
import java.util.List;

public abstract class AppleBufferedLogger implements AppleLoggerName {
    private final List<LogMessage> messages = new ArrayList<>();

    @Override
    public void logInternal(LogMessage message) {
        synchronized (messages) {
            this.messages.add(message);
        }
    }

    public void flush() {
        synchronized (messages) {
            flush(messages);
            messages.clear();
        }
    }

    protected abstract void flush(List<LogMessage> messages);
}
