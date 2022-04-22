package apple.utilities.threading.util.function;

@FunctionalInterface
public interface FunctionUncaught<R, T> {
    R accept(T obj) throws Exception;
}
