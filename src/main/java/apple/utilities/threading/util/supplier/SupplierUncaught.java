package apple.utilities.threading.util.supplier;

@FunctionalInterface
public interface SupplierUncaught<T> {
    T get() throws Exception;
}
