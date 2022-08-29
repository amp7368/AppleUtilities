package apple.utilities.threading.service.base.task;

import apple.utilities.threading.service.base.failure.TaskFailureProcedure;
import apple.utilities.threading.util.ThreadFactory;
import java.util.Objects;
import java.util.function.Consumer;

public record AsyncTaskLife<T, Extra>(AsyncTask<T, Extra> task, Consumer<T> onSuccess,
                                      Consumer<Exception> onFailure) implements ThreadFactory {

    public AsyncTaskLife(AsyncTask<T, Extra> task, Consumer<T> onSuccess,
        Consumer<Exception> onFailure) {
        this.task = task;
        this.onSuccess = Objects.requireNonNullElse(onSuccess, emptyConsumer());
        this.onFailure = Objects.requireNonNullElse(onFailure, Exception::printStackTrace);
    }

    public Extra extra() {
        return task.extra();
    }

    public T get() throws Exception {
        return task.get();
    }

    public TaskFailureProcedure failureProcedure() {
        return task.failureProcedure();
    }

    public void onSuccess(T gotten) {
        onSuccess.accept(gotten);
    }

    public void onFailure(Exception exception) {
        onFailure.accept(exception);
    }
}
