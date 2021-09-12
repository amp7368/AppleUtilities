package apple.utilities.request.keyed;

import apple.utilities.request.AppleRequest;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.settings.RequestSettingsBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface AppleRequestKeyQueue<T> {
    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> request) {
        return queue(id, request, (t) -> {
        }, getDefaultSettings());
    }

    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> request, @Nullable RequestSettingsBuilder<T> builder) {
        return queue(id, request, (t) -> {
        }, builder);
    }


    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> request, @Nullable Consumer<T> runAfter) {
        return queue(id, request, runAfter, getDefaultSettings());
    }

    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> newRequest, @Nullable Consumer<T> runAfter, @Nullable RequestSettingsBuilder<T> builder) {
        return queue(id, newRequest, runAfter, builder, (o, n) -> n);
    }


    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> request, AppleRequestOnConflict<T> onConflict) {
        return queue(id, request, (t) -> {
        }, getDefaultSettings(), onConflict);
    }

    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> request, @Nullable RequestSettingsBuilder<T> builder, AppleRequestOnConflict<T> onConflict) {
        return queue(id, request, (t) -> {
        }, builder, onConflict);
    }


    default AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> request, @Nullable Consumer<T> runAfter, AppleRequestOnConflict<T> onConflict) {
        return queue(id, request, runAfter, getDefaultSettings(), onConflict);
    }


    AppleRequestService.RequestHandler<T> queue(Object id, AppleRequest<T> newRequest, @Nullable Consumer<T> runAfter, @Nullable RequestSettingsBuilder<T> builder, AppleRequestOnConflict<T> requestConflict);

    RequestSettingsBuilder<T> getDefaultSettings();
}
