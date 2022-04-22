package apple.utilities.threading.util.runnable;

import java.util.function.Supplier;

@FunctionalInterface
public interface RunnableSupplier extends Runnable, Supplier<Object> {
    @Override
    default Object get() {
        this.run();
        return null;
    }
}
