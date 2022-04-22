package apple.utilities.request.settings;

import apple.utilities.request.ExceptionHandler;
import apple.utilities.request.RequestLogger;
import apple.utilities.structures.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Deprecated
public class RequestSettingsBuilder<T> {
    private List<Pair<ExceptionHandler, Integer>> exceptionHandler = new ArrayList<>();
    private RequestLogger<T> requestLogger = RequestLogger.empty();

    public RequestSettingsBuilder() {
    }

    public RequestSettingsBuilder(RequestSettingsBuilder<T> other) {
        this.exceptionHandler = other.exceptionHandler;
        this.requestLogger = other.requestLogger;
    }

    public static <T> RequestSettingsBuilder<T> empty() {
        return new RequestSettingsBuilder<>();
    }

    public RequestSettingsBuilder<T> withExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = new ArrayList<>(List.of(new Pair<>(exceptionHandler, 0)));
        return this;
    }

    public RequestSettingsBuilder<T> addExceptionHandler(ExceptionHandler exceptionHandler, int priority) {
        this.exceptionHandler.add(new Pair<>(exceptionHandler, priority));
        return this;
    }

    public RequestSettingsBuilder<T> withRequestLogger(RequestLogger<T> requestLogger) {
        this.requestLogger = requestLogger;
        return this;
    }

    public RequestSettingsBuilder<T> addRequestLogger(RequestLogger<T> requestLogger) {
        RequestLogger<T> oldLogger = this.requestLogger;
        this.requestLogger = new RequestLogger<T>() {
            @Override
            public void startRequest() {
                oldLogger.startRequest();
                requestLogger.startRequest();
            }

            @Override
            public void exceptionHandle(Exception e) {
                oldLogger.exceptionHandle(e);
                requestLogger.exceptionHandle(e);
            }

            @Override
            public void finishDone(T gotten) {
                oldLogger.finishDone(gotten);
                requestLogger.finishDone(gotten);
            }

            @Override
            public void startDone(T gotten) {
                oldLogger.startDone(gotten);
                requestLogger.startDone(gotten);
            }
        };
        return this;
    }

    public List<Pair<ExceptionHandler, Integer>> getExceptionHandler() {
        this.exceptionHandler.sort(Comparator.comparingInt(Pair::getValue));
        return this.exceptionHandler;
    }

    public RequestLogger<T> getLogger() {
        return this.requestLogger;
    }
}
