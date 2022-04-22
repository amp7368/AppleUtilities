package apple.utilities.threading.service.priority;

import apple.utilities.threading.service.base.handler.TaskHandler;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TaskHandlerPriority extends TaskHandler<AsyncTaskPriority> {
    private final Map<Integer, List<AsyncTaskAttempt<?, AsyncTaskPriority>>> tasks = new HashMap<>();
    private final List<Integer> priorities = new ArrayList<>();

    @Override
    protected AsyncTaskPriority defaultTaskExtra() {
        return new AsyncTaskPriority(0);
    }

    @Override
    protected @Nullable AsyncTaskAttempt<?, AsyncTaskPriority> popTask() {
        priorities.sort(Comparator.naturalOrder());
        for (int priority : this.priorities) {
            List<AsyncTaskAttempt<?, AsyncTaskPriority>> tasksByPriority = tasks.get(priority);
            if (tasksByPriority != null && !tasksByPriority.isEmpty()) {
                AsyncTaskAttempt<?, AsyncTaskPriority> task = tasksByPriority.remove(0);
                if (tasksByPriority.isEmpty()) this.tasks.remove(priority);
                this.priorities.removeIf(p -> !tasks.containsKey(p));
                return task;
            }
        }
        return null;
    }

    @Override
    protected long timeToNextRequest() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    protected <T> void add(AsyncTaskAttempt<T, AsyncTaskPriority> asyncTask) {
        int priority = asyncTask.task().extra().getPriority();
        if (!this.priorities.contains(priority))
            priorities.add(priority);
        tasks.compute(priority, (key, queue) -> {
            if (queue == null) queue = new ArrayList<>();
            queue.add(asyncTask);
            return queue;
        });
    }
}
