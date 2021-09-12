package apple.utilities.request;

@FunctionalInterface
public interface ExceptionHandler {
    ExceptionHandler EMPTY = ExceptionHandler::throwE;

    static void throwE(Exception e) {
        throw new AppleRequest.AppleRuntimeRequestException(e);
    }

    void accept(Exception e) throws AppleRequest.AppleRuntimeRequestException;

    default ExceptionHandler orThen(ExceptionHandler other) {
        return (e) -> {
            try {
                this.accept(e);
            } catch (AppleRequest.AppleRuntimeRequestException e1) {
                other.accept(e);
            }
        };
    }

    default ExceptionHandler andThen(ExceptionHandler other) {
        return (e) -> {
            try {
                this.accept(e);
            } finally {
                other.accept(e);
            }
        };
    }
}
