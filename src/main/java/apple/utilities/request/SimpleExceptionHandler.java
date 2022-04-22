package apple.utilities.request;

import apple.utilities.util.ExceptionUnpackaging;

@Deprecated
public class SimpleExceptionHandler implements ExceptionHandler {
    private static final SimpleExceptionHandler instance = new SimpleExceptionHandler();
    private final Class<?>[] ignored;
    private Runnable runAfter;

    private SimpleExceptionHandler() {
        ignored = new Class[0];
    }

    public SimpleExceptionHandler(Class<?>[] ignored) {
        this.ignored = ignored;
        this.runAfter = null;
    }

    public SimpleExceptionHandler(Class<?>[] ignored, Runnable runAfter) {
        this.ignored = ignored;
        this.runAfter = runAfter;
    }

    public static SimpleExceptionHandler getInstance() {
        return instance;
    }

    @Override
    public void accept(Exception e) throws AppleRequest.AppleRuntimeRequestException {
        if (ExceptionUnpackaging.exists(e, ignored)) {
            if (runAfter != null) runAfter.run();
            return;
        }
        if (runAfter != null) runAfter.run();
        throw new AppleRequest.AppleRuntimeRequestException(e.getMessage(), e);
    }
}
