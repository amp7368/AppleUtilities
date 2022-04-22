package apple.utilities.threading.util.runnable;

@FunctionalInterface
public interface RunnableUncaught {
    void run() throws Exception;
}
