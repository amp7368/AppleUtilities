package apple.utilities.threading.service.queue;

import apple.utilities.threading.service.base.handler.TaskHandler;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TaskHandlerQueue extends TaskHandler<AsyncTaskQueue> {
    protected final List<AsyncTaskAttempt<?, AsyncTaskQueue>> tasks = new ArrayList<>();
    private final int tasksPerInterval;
    private final int intervalTime;
    private final int safeGuardBuffer;

    public TaskHandlerQueue(int tasksPerInterval, int intervalTime, int safeGuardBuffer) {
        this.tasksPerInterval = tasksPerInterval;
        this.intervalTime = intervalTime;
        this.safeGuardBuffer = safeGuardBuffer;
    }

    @Override
    protected AsyncTaskQueue defaultTaskExtra() {
        return AsyncTaskQueue.get();
    }

    @Override
    protected @Nullable AsyncTaskAttempt<?, AsyncTaskQueue> popTask() {
        synchronized (this.isRunningLock) {
            if (this.tasks.isEmpty()) return null;
            return this.tasks.remove(0);
        }
    }

    @Override
    protected long timeToNextRequest() {
        synchronized (this.oldTasks) {
            this.trimOldTasks(System.currentTimeMillis() + this.intervalTime);
            int tasksToExpire = this.oldTasks.size() - tasksPerInterval - 1;
            if (tasksToExpire <= 0) return this.safeGuardBuffer;
            return this.calculateTimeToExpire(tasksToExpire);
        }
    }

    @Override
    protected <T> void add(AsyncTaskAttempt<T, AsyncTaskQueue> asyncTask) {
        synchronized (tasks) {
            this.tasks.add(asyncTask);
        }
    }

}
