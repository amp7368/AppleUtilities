package apple.utilities.threading.cached;

import java.util.concurrent.Executor;

public class DelegatingExecutorLayer {

    private final int maxCount;
    private int runningCount = 1;
    private final Executor executor;

    public DelegatingExecutorLayer(Executor executor, int maxCount) {
        this.executor = executor;
        this.maxCount = maxCount;
    }


    public void countDown() {
        synchronized (this) {
            runningCount--;
        }
    }

    public boolean tryCountUp() {
        synchronized (this) {
            if (runningCount == maxCount) return false;
            runningCount++;
            return true;
        }
    }

    public Executor getExecutor() {
        return executor;
    }
}
