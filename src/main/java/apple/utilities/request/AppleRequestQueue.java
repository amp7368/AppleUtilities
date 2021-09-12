package apple.utilities.request;

import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;

import java.util.function.Consumer;

public interface AppleRequestQueue {
    default AppleRequestService.RequestHandler<?> queueVoid(AppleRequestVoid request) {
        return queueVoid(request, () -> {
        }, RequestSettingsBuilderVoid.VOID);
    }

    default AppleRequestService.RequestHandler<?> queueVoid(AppleRequestVoid request, Runnable runAfter) {
        return queueVoid(request, runAfter, RequestSettingsBuilderVoid.VOID);
    }

    default AppleRequestService.RequestHandler<?> queueVoid(AppleRequestVoid request, RequestSettingsBuilderVoid builder) {
        return queueVoid(request, () -> {
        }, builder);
    }

    default AppleRequestService.RequestHandler<Boolean> queueVoid(AppleRequestVoid request, Runnable runAfter, RequestSettingsBuilderVoid builder) {
        return this.queue(request, t -> runAfter.run(), new RequestSettingsBuilder<>(builder));
    }

    default <T> AppleRequestService.RequestHandler<T> queue(AppleRequest<T> request, Consumer<T> runAfter) {
        return queue(request, runAfter, getDefaultSettings());
    }

    <T> RequestSettingsBuilder<T> getDefaultSettings();

    <T> AppleRequestService.RequestHandler<T> queue(AppleRequest<T> request, Consumer<T> runAfter, RequestSettingsBuilder<T> builder);
}
