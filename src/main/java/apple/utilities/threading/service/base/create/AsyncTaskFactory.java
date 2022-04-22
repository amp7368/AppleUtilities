package apple.utilities.threading.service.base.create;

import apple.utilities.threading.service.base.failure.TaskFailureBinaryRequeue;
import apple.utilities.threading.service.base.failure.TaskFailureNoRequeue;
import apple.utilities.threading.service.base.failure.TaskFailureProcedure;
import apple.utilities.threading.service.base.task.AsyncTask;
import apple.utilities.threading.util.supplier.SupplierUncaught;

public interface AsyncTaskFactory {
    AsyncTaskFactory instance = new AsyncTaskFactory() {
    };

    static AsyncTaskFactory get() {
        return instance;
    }

    default AsyncTaskExtraFactory extra() {
        return AsyncTaskExtraFactory.get();
    }

    default AsyncTaskFailureFactory failure() {
        return AsyncTaskFailureFactory.get();
    }

    default <Extra> AsyncTaskCreate<Extra> taskWith(Extra extra, TaskFailureProcedure failureProcedure) {
        return new AsyncTaskCreate<>() {
            @Override
            public <T> AsyncTask<T, Extra> create(SupplierUncaught<T> task) {
                return new AsyncTask<>(task, extra, failureProcedure);
            }
        };
    }

    interface AsyncTaskExtraFactory {
        AsyncTaskExtraFactory instance = new AsyncTaskExtraFactory() {
        };

        static AsyncTaskExtraFactory get() {
            return instance;
        }
    }

    interface AsyncTaskFailureFactory {
        AsyncTaskFailureFactory instance = new AsyncTaskFailureFactory() {
        };

        static AsyncTaskFailureFactory get() {
            return instance;
        }

        default TaskFailureProcedure binaryRequeue(long startingFailTime) {
            return new TaskFailureBinaryRequeue(startingFailTime);
        }

        default TaskFailureProcedure noRequeue() {
            return new TaskFailureNoRequeue();
        }
    }
}

