package apple.utilities.request.keyed;

import apple.utilities.request.AppleRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


@FunctionalInterface
public interface AppleRequestOnConflict<R> {
    @Contract(pure = true)
    static <T> @NotNull AppleRequestOnConflict<T> ADD() {
        return AppleRequest::andThen;
    }

    @Contract(pure = true)
    static <T> @NotNull AppleRequestOnConflict<T> REPLACE() {
        return (o, n) -> n;
    }

    AppleRequest<R> onConflict(AppleRequest<R> oldRequest, AppleRequest<R> newRequest);
}
