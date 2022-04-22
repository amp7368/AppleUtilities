package apple.utilities.threading.util.consumer;

import java.util.function.Consumer;

public interface ConsumerFactory {
    default <T> Consumer<T> emptyConsumer() {
        return (t) -> {
        };
    }
}
