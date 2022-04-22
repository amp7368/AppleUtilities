package apple.utilities.threading.service.base.task;

import apple.utilities.threading.service.base.failure.TaskFailureProcedure;
import apple.utilities.threading.util.supplier.SupplierUncaught;

public record AsyncTask<T, Extra>(SupplierUncaught<T> supplier,
                                  Extra extra,
                                  TaskFailureProcedure failureProcedure) {
    public T get() throws Exception {
        return this.supplier.get();
    }
}
