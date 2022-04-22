package apple.utilities.threading.util.consumer;

import apple.utilities.threading.util.runnable.RunnableITW;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConsumerITW<T> extends Consumer<T> {
    default RunnableITW asRunnable(T consume) {
        return () -> this.accept(consume);
    }
}
