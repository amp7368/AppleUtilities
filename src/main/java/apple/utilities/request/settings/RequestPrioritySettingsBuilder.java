package apple.utilities.request.settings;

import apple.utilities.request.AppleRequestPriorityService;
import apple.utilities.request.ExceptionHandler;
import apple.utilities.request.RequestLogger;
import org.jetbrains.annotations.Nullable;

public class RequestPrioritySettingsBuilder<T, Priority extends AppleRequestPriorityService.AppleRequestPriority> extends RequestSettingsBuilder<T> {
    private Priority priority = null;

    public RequestPrioritySettingsBuilder() {
    }

    public RequestPrioritySettingsBuilder(RequestPrioritySettingsBuilder<T, Priority> settingsBuilder) {
        super(settingsBuilder);
        this.priority = settingsBuilder.priority;
    }

    public RequestPrioritySettingsBuilder(RequestSettingsBuilder<T> settingsBuilder) {
        super(settingsBuilder);
    }

    public static <T, Priority extends AppleRequestPriorityService.AppleRequestPriority> RequestPrioritySettingsBuilder<T, Priority> emptyPriority() {
        return new RequestPrioritySettingsBuilder<>();
    }

    @Nullable
    public Priority getPriority() {
        return this.priority;
    }

    public RequestPrioritySettingsBuilder<T, Priority> withPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public RequestPrioritySettingsBuilder<T, Priority> withPriorityExceptionHandler(ExceptionHandler exceptionHandler) {
        super.withExceptionHandler(exceptionHandler);
        return this;
    }

    public RequestPrioritySettingsBuilder<T, Priority> withPriorityRequestLogger(RequestLogger<T> requestLogger) {
        super.withRequestLogger(requestLogger);
        return this;
    }
}
