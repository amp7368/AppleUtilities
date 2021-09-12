package apple.utilities.request.keyed;

import apple.utilities.request.AppleRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


@FunctionalInterface
public interface AppleRequestOnConflict<T> {
    @Contract(pure = true)
    static <T> @NotNull AppleRequestOnConflict<T> ADD() {
        return AppleRequest::andThen;
    }

    @Contract(pure = true)
    static <T> @NotNull AppleRequestOnConflict<T> REPLACE() {
        return (o, n) -> n;
    }

    AppleRequest<T> onConflict(AppleRequest<T> oldRequest, AppleRequest<T> newRequest);
}
