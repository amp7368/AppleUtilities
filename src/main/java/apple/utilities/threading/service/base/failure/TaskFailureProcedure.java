package apple.utilities.threading.service.base.failure;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class TaskFailureProcedure {
    private static final ScheduledThreadPoolExecutor requeueThreadPool = new ScheduledThreadPoolExecutor(1);
    protected int failCount = 0;

    public void incrementFail() {
        this.failCount++;
    }

    public abstract boolean shouldRetryNow();

    public abstract void finishFailure(Runnable queueToHandler);

    protected void schedule(Runnable runnable, long timeToSleep) {
        requeueThreadPool.schedule(runnable, timeToSleep, TimeUnit.MILLISECONDS);
    }
}
