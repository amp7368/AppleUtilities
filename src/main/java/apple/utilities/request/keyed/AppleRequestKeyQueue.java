package apple.utilities.request.keyed;

import apple.utilities.request.AppleRequest;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.settings.RequestSettingsBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Deprecated
public interface AppleRequestKeyQueue<RequestType> {
    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> request) {
        return queue(id, request, (t) -> {
        }, getDefaultSettings());
    }

    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> request, @Nullable RequestSettingsBuilder<RequestType> builder) {
        return queue(id, request, (t) -> {
        }, builder);
    }


    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> request, @Nullable Consumer<RequestType> runAfter) {
        return queue(id, request, runAfter, getDefaultSettings());
    }

    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> newRequest, @Nullable Consumer<RequestType> runAfter, @Nullable RequestSettingsBuilder<RequestType> builder) {
        return queue(id, newRequest, runAfter, builder, (o, n) -> n);
    }


    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> request, AppleRequestOnConflict<RequestType> onConflict) {
        return queue(id, request, (t) -> {
        }, getDefaultSettings(), onConflict);
    }

    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> request, @Nullable RequestSettingsBuilder<RequestType> builder, AppleRequestOnConflict<RequestType> onConflict) {
        return queue(id, request, (t) -> {
        }, builder, onConflict);
    }


    default AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> request, @Nullable Consumer<RequestType> runAfter, AppleRequestOnConflict<RequestType> onConflict) {
        return queue(id, request, runAfter, getDefaultSettings(), onConflict);
    }


    AppleRequestService.RequestHandler<RequestType> queue(Object id, AppleRequest<RequestType> newRequest, @Nullable Consumer<RequestType> runAfter, @Nullable RequestSettingsBuilder<RequestType> builder, AppleRequestOnConflict<RequestType> requestConflict);

    RequestSettingsBuilder<RequestType> getDefaultSettings();
}
