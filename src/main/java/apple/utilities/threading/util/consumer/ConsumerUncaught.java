package apple.utilities.threading.util.consumer;

@FunctionalInterface
public interface ConsumerUncaught<T> {
    void accept(T obj) throws Exception;
}
