package apple.utilities.request.settings;

public class RequestSettingsBuilderVoid extends RequestSettingsBuilder<Boolean> {
    public static final RequestSettingsBuilderVoid VOID = new RequestSettingsBuilderVoid();

    public RequestSettingsBuilderVoid() {
    }

    public static RequestSettingsBuilderVoid empty() {
        return new RequestSettingsBuilderVoid();
    }
}
