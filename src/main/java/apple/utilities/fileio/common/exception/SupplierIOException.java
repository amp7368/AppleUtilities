package apple.utilities.fileio.common.exception;

import java.io.IOException;

@FunctionalInterface
public interface SupplierIOException<T> {
    T get() throws IOException;
}
