package apple.utilities.threading.util.runnable;

public interface RunnableFactory {
    RunnableSupplier EMPTY_RUNNABLE = () -> {
    };

    default RunnableSupplier emptyRunnable() {
        return EMPTY_RUNNABLE;
    }
}
