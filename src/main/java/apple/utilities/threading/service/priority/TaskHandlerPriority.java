package apple.utilities.threading.service.priority;

import apple.utilities.threading.service.base.handler.TaskHandler;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

public class TaskHandlerPriority extends TaskHandler<AsyncTaskPriority> {

    private final Map<Integer, List<AsyncTaskAttempt<?, AsyncTaskPriority>>> tasks = new HashMap<>();

    private final int tasksPerInterval;
    private final int intervalTime;
    private final int safeGuardBuffer;

    public TaskHandlerPriority(int tasksPerInterval, int intervalTime, int safeGuardBuffer) {
        this.tasksPerInterval = tasksPerInterval;
        this.intervalTime = intervalTime;
        this.safeGuardBuffer = safeGuardBuffer;
    }


    @Override
    protected AsyncTaskPriority defaultTaskExtra() {
        return new AsyncTaskPriority(0);
    }

    @Override
    protected @Nullable AsyncTaskAttempt<?, AsyncTaskPriority> popTask() {
        List<Entry<Integer, List<AsyncTaskAttempt<?, AsyncTaskPriority>>>> entries = new ArrayList<>(this.tasks.entrySet());
        entries.sort(Comparator.comparingInt(Entry::getKey));
        for (Entry<Integer, List<AsyncTaskAttempt<?, AsyncTaskPriority>>> priority : entries) {
            List<AsyncTaskAttempt<?, AsyncTaskPriority>> tasksByPriority = priority.getValue();
            if (tasksByPriority.isEmpty()) {
                this.tasks.remove(priority.getKey());
                continue;
            }
            return tasksByPriority.remove(0);
        }
        return null;
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
    protected <T> void add(AsyncTaskAttempt<T, AsyncTaskPriority> asyncTask) {
        int priority = asyncTask.task().extra().getPriority();
        tasks.compute(priority, (key, queue) -> {
            if (queue == null) queue = new ArrayList<>();
            queue.add(asyncTask);
            return queue;
        });
    }
}
