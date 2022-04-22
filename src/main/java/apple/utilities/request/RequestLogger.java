package apple.utilities.request;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Deprecated
public interface RequestLogger<T> {
    @Contract(value = " -> new", pure = true)
    static <T> @NotNull RequestLogger<T> empty() {
        return new RequestLogger<>() {
        };
    }

    default void startRequest() {
    }

    default void exceptionHandle(Exception e) {
    }

    default void exceptionUncaught(Exception e) {
    }

    default void finishDone(T gotten) {
    }

    default void startDone(T gotten) {
    }
}
