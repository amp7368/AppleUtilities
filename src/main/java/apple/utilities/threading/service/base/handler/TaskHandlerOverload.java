package apple.utilities.threading.service.base.handler;

import apple.utilities.threading.service.base.task.AsyncTask;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.service.base.task.AsyncTaskLife;
import apple.utilities.threading.util.ThreadFactory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface TaskHandlerOverload<TaskExtra> extends ThreadFactory {
    default <T> AsyncTaskAttempt<T, TaskExtra> accept(AsyncTask<T, TaskExtra> task) {
        return accept(task, null, null);
    }

    default <T> AsyncTaskAttempt<T, TaskExtra> accept(AsyncTask<T, TaskExtra> task, @Nullable Consumer<T> onSuccess) {
        return accept(task, onSuccess, null);
    }

    default <T> AsyncTaskAttempt<T, TaskExtra> accept(AsyncTask<T, TaskExtra> task, @Nullable Consumer<T> onSuccess, @Nullable Consumer<Exception> onFailure) {
        AsyncTaskLife<T, TaskExtra> taskLife = new AsyncTaskLife<>(task, onSuccess, onFailure);
        return accept(taskLife);
    }

    <T> AsyncTaskAttempt<T, TaskExtra> accept(AsyncTaskLife<T, TaskExtra> taskLife);
}
