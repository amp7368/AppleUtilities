package apple.utilities.request;
@Deprecated
public interface AppleRequestVoid extends AppleRequest<Boolean> {
    AppleRequestVoid EMPTY = () -> {
    };

    static AppleRequestVoid wrapper(AppleRequest<?> request) {
        return request::get;
    }

    void complete() throws AppleRequest.AppleRequestException;

    @Override
    default Boolean get() throws AppleRequestException {
        complete();
        return true;
    }
}
