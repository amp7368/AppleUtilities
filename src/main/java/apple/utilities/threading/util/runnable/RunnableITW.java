package apple.utilities.threading.util.runnable;

import apple.utilities.threading.util.supplier.SupplierITW;

@FunctionalInterface
public interface RunnableITW extends Runnable {
    default <T> SupplierITW<T> asEmptySupplier() {
        return () -> {
            this.run();
            return null;
        };
    }
}
