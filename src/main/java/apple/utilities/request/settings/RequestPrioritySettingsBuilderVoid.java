package apple.utilities.request.settings;

import apple.utilities.request.AppleRequestPriorityService;

public class RequestPrioritySettingsBuilderVoid<Priority extends AppleRequestPriorityService.AppleRequestPriority>
        extends RequestPrioritySettingsBuilder<Boolean, Priority> {
    public RequestPrioritySettingsBuilderVoid() {
    }

    public RequestPrioritySettingsBuilderVoid(RequestPrioritySettingsBuilder<Boolean, Priority> settingsBuilder) {
        super(settingsBuilder);
    }

    public static <P extends AppleRequestPriorityService.AppleRequestPriority> RequestPrioritySettingsBuilderVoid<P> emptyVoid() {
        return new RequestPrioritySettingsBuilderVoid<>();
    }
}
