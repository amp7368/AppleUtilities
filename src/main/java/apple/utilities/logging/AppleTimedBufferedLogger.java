package apple.utilities.logging;

@Deprecated
public abstract class AppleTimedBufferedLogger extends AppleBufferedLogger {

    private final long sleepTime;
    private boolean isRunning = false;

    protected AppleTimedBufferedLogger(long sleepTime) {
        this.sleepTime = sleepTime;
    }


    @Override
    public void logInternal(LogMessage logMessage) {
        super.logInternal(logMessage);
        synchronized (this) {
            if (!isRunning) {
                isRunning = true;
                startRunning();
            }
        }
    }

    protected void startRunning() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            isRunning = false;
            flush();
        }
    }
}
