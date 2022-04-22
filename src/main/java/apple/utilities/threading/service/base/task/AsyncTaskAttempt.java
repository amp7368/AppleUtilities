package apple.utilities.threading.service.base.task;

import apple.utilities.threading.service.base.failure.TaskFailureProcedure;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AsyncTaskAttempt<T, Extra> {
    private final Object runningSync = new Object();
    private final AsyncTaskLife<T, Extra> task;
    private final List<Consumer<T>> onSuccess = new ArrayList<>(1);
    private final List<Consumer<Exception>> onFailure = new ArrayList<>(1);
    private boolean isComplete = false;
    private T gotten = null;
    private boolean isLastAttemptFail = true;
    private Exception exception = null;

    public AsyncTaskAttempt(AsyncTaskLife<T, Extra> task) {
        this.task = task;
    }

    public void run() {
        T gottenTemp;
        boolean wasFail;
        Exception exceptionTemp = null;
        synchronized (runningSync) {
            try {
                gottenTemp = this.task.get();
                wasFail = false;
            } catch (Exception e) {
                this.task.failureProcedure().incrementFail();
                exceptionTemp = e;
                gottenTemp = null;
                wasFail = true;
            }
        }
        synchronized (this) {
            this.gotten = gottenTemp;
            this.isLastAttemptFail = wasFail;
            this.exception = exceptionTemp;
            this.isComplete = true;
            this.notifyAll();
        }
        if (wasFail) {
            synchronized (onFailure) {
                onFailure.forEach((c) -> c.accept(this.exception));
            }
        } else {
            synchronized (onSuccess) {
                onSuccess.forEach((c) -> c.accept(this.gotten));
            }
        }
    }

    public T complete() {
        synchronized (this) {
            if (!isComplete) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return gotten;
        }
    }

    public void onSuccess(Consumer<T> onSuccess) {
        synchronized (this.onSuccess) {
            this.onSuccess.add(onSuccess);
        }
        synchronized (this) {
            if (isComplete && !isLastAttemptFail) onSuccess.accept(this.gotten);
        }
    }

    public void onFailure(Consumer<Exception> onFailure) {
        synchronized (this.onFailure) {
            this.onFailure.add(onFailure);
        }
        synchronized (this) {
            if (isComplete && !isLastAttemptFail) onFailure.accept(this.exception);
        }
    }


    @Nullable
    public T getValueNow() {
        synchronized (this) {
            return this.gotten;
        }
    }

    public boolean isRecentTryFail() {
        synchronized (this) {
            return this.isLastAttemptFail;
        }
    }

    public TaskFailureProcedure failureProcedure() {
        return this.task.failureProcedure();
    }

    public AsyncTaskLife<T, Extra> task() {
        return this.task;
    }
}
