package apple.utilities.threading.service.base.handler;

import apple.utilities.threading.service.base.create.AsyncTaskCreate;
import apple.utilities.threading.service.base.create.AsyncTaskFactory;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.failure.TaskFailureProcedure;
import apple.utilities.threading.service.base.task.AsyncTask;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.service.base.task.AsyncTaskLife;
import apple.utilities.threading.service.base.task.OldTask;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public abstract class TaskHandler<TaskExtra> implements TaskHandlerOverload<TaskExtra> {

    protected final Object isRunningLock = new Object();
    protected final Collection<OldTask<?>> oldTasks = new ArrayList<>();
    private final RateLimit rateLimit = new RateLimit();
    private boolean isRunningRequests = false;
    private @Nullable AsyncTaskAttempt<?, TaskExtra> nextTask = null;

    public <T> AsyncTaskAttempt<T, TaskExtra> accept(AsyncTaskLife<T, TaskExtra> taskLife) {
        AsyncTaskAttempt<T, TaskExtra> asyncTask = newAttempt(taskLife);
        add(asyncTask);
        verifyRunning();
        return asyncTask;
    }

    public AsyncTaskQueueStart<TaskExtra> taskCreator(TaskExtra extra, TaskFailureProcedure failureProcedure) {
        return new AsyncTaskCreate<TaskExtra>() {
            @Override
            public <T> AsyncTask<T, TaskExtra> create(SupplierUncaught<T> task) {
                return new AsyncTask<>(task, extra, failureProcedure);
            }
        }.withHandler(this);
    }

    public AsyncTaskQueueStart<TaskExtra> taskCreator(TaskExtra extra) {
        return this.taskCreator(extra, defaultFailureProcedure());
    }

    protected TaskFailureProcedure defaultFailureProcedure() {
        return AsyncTaskFactory.get().failure().noRequeue();
    }

    public AsyncTaskQueueStart<TaskExtra> taskCreator() {
        return this.taskCreator(defaultTaskExtra(), defaultFailureProcedure());
    }

    protected abstract TaskExtra defaultTaskExtra();

    protected <T> void addNext(AsyncTaskLife<T, TaskExtra> task) {
        this.nextTask = newAttempt(task);
        verifyRunning();
    }

    private void verifyRunning() {
        synchronized (isRunningLock) {
            if (this.isRunningRequests) return;
            isRunningRequests = true;
            new Thread(this::processTasks).start();
        }
    }

    private void processTasks() {
        while (true) {
            @Nullable AsyncTaskAttempt<?, TaskExtra> task;
            synchronized (this.isRunningLock) {
                if (this.nextTask != null) {
                    task = this.nextTask;
                    this.nextTask = null;
                } else task = this.popTask();
                if (task == null) {
                    this.isRunningRequests = false;
                    return;
                }
            }
            runTask(task);
        }
    }

    private <T> void runTask(AsyncTaskAttempt<T, TaskExtra> attempt) {
        attempt.run();
        if (attempt.isRecentTryFail()) {
            TaskFailureProcedure failureHandler = attempt.failureProcedure();
            if (failureHandler.shouldRetryNow())
                this.addNext(attempt.task());
            failureHandler.finishFailure(() -> this.accept(attempt.task()));
        }
        setOld(attempt);
        doSleep();
    }

    private void doSleep() {
        long timeToNextRequest = Math.max(this.rateLimit.getDelay(), this.timeToNextRequest());
        if (timeToNextRequest <= 0) return;
        try {
            Thread.sleep(timeToNextRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private <T> void setOld(AsyncTaskAttempt<T, TaskExtra> task) {
        synchronized (this.oldTasks) {
            this.oldTasks.add(new OldTask<>(task.getValueNow()));
        }
    }


    @Nullable
    protected abstract AsyncTaskAttempt<?, TaskExtra> popTask();

    protected abstract long timeToNextRequest();

    protected abstract <T> void add(AsyncTaskAttempt<T, TaskExtra> asyncTask);

    protected void trimOldTasks(long oldTrimTime) {
        synchronized (this.oldTasks) {
            this.oldTasks.removeIf(oldTask -> oldTask.getTime() < oldTrimTime);
        }
    }

    protected long calculateTimeToExpire(int tasksToExpire) {
        synchronized (this.oldTasks) {
            // newest tasks at the front
            List<Long> expireTasks = new ArrayList<>(tasksToExpire + 1);
            for (OldTask<?> task : this.oldTasks) {
                if (expireTasks.size() < tasksToExpire) {
                    expireTasks.add(task.getTime());
                    expireTasks.sort(Comparator.reverseOrder());
                } else if (task.getTime() < expireTasks.get(0)) {
                    expireTasks.add(task.getTime());
                    expireTasks.sort(Comparator.reverseOrder());
                    expireTasks.remove(0);
                }
            }
            if (expireTasks.isEmpty()) return 0;
            return System.currentTimeMillis() - expireTasks.get(0);
        }
    }

    private <T> AsyncTaskAttempt<T, TaskExtra> newAttempt(AsyncTaskLife<T, TaskExtra> taskLife) {
        return new AsyncTaskAttempt<>(taskLife);
    }

    private RateLimit getRateLimit() {
        return this.rateLimit;
    }
}
