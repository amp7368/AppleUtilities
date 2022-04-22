package apple.utilities.threading.service.base.create;

import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.util.supplier.SupplierUncaught;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface AsyncTaskQueueStart<Extra> {
    <T> AsyncTaskAttempt<T, Extra> accept(SupplierUncaught<T> task, Consumer<T> onSuccess, Consumer<Exception> onFailure);

    default <T> AsyncTaskAttempt<T, Extra> accept(SupplierUncaught<T> task, Consumer<T> onSuccess) {
        return this.accept(task, onSuccess, null);
    }

    default <T> AsyncTaskAttempt<T, Extra> accept(SupplierUncaught<T> task) {
        return this.accept(task, null, null);
    }

    default <T> AsyncTaskAttempt<T, Extra> acceptCaught(Supplier<T> task, Consumer<T> onSuccess, Consumer<Exception> onFailure) {
        return this.accept(task::get, onSuccess, onFailure);
    }
}
