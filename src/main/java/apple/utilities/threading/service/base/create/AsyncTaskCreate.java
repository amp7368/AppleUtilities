package apple.utilities.threading.service.base.create;

import apple.utilities.threading.service.base.handler.TaskHandler;
import apple.utilities.threading.service.base.task.AsyncTask;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface AsyncTaskCreate<Extra> {

    <T> AsyncTask<T, Extra> create(SupplierUncaught<T> task);

    default <T> AsyncTask<T, Extra> createCaught(Supplier<T> task) {
        return this.create(task::get);
    }

    default AsyncTaskQueueStart<Extra> withHandler(TaskHandler<Extra> handler) {
        AsyncTaskCreate<Extra> outerThis = this;
        return new AsyncTaskQueueStart<>() {
            @Override
            public <T> AsyncTaskAttempt<T, Extra> accept(SupplierUncaught<T> task,
                Consumer<T> onSuccess, Consumer<Exception> onFailure) {
                return handler.accept(outerThis.create(task), onSuccess, onFailure);
            }
        };
    }
}
