package apple.utilities.request;

public interface VoidRequestLogger extends RequestLogger<Boolean> {
    VoidRequestLogger EMPTY = new VoidRequestLogger() {
    };

    default void startDone() {
    }

    default void finishDone() {
    }

    default void startDone(Boolean b) {
        startDone();
    }

    default void finishDone(Boolean b) {
        finishDone();
    }

}
