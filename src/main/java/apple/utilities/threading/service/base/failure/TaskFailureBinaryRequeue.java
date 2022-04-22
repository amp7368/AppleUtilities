package apple.utilities.threading.service.base.failure;

public class TaskFailureBinaryRequeue extends TaskFailureProcedure {
    private final long startingFailTime;

    public TaskFailureBinaryRequeue(long startingFailTime) {
        this.startingFailTime = startingFailTime;
    }

    @Override
    public boolean shouldRetryNow() {
        return false;
    }

    @Override
    public void finishFailure(Runnable queueToHandler) {
        schedule(queueToHandler, failTimeToSleep());
    }

    public long failTimeToSleep() {
        return (long) (this.startingFailTime * Math.pow(2, failCount));
    }
}
