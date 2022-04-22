package apple.utilities.fileio.common.exception;

import java.io.IOException;

@FunctionalInterface
public interface ConsumerIOException<T> {
    void accept(T obj) throws IOException;
}
