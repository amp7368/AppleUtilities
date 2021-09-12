package apple.utilities.logging;

public abstract class AppleTimedBufferedLogger extends AppleBufferedLogger {
    private boolean isRunning = false;
    private long sleepTime;

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
