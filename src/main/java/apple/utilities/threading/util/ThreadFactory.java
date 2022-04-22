package apple.utilities.threading.util;

import apple.utilities.threading.util.consumer.ConsumerFactory;
import apple.utilities.threading.util.function.FunctionFactory;
import apple.utilities.threading.util.runnable.RunnableFactory;
import apple.utilities.threading.util.supplier.SupplierFactory;

public interface ThreadFactory extends ConsumerFactory, FunctionFactory, RunnableFactory, SupplierFactory {
    static ThreadFactory get() {
        return new ThreadFactory() {
        };
    }

    default Thread thread(Runnable runnable) {
        return new Thread(runnable);
    }

    default void startThread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
