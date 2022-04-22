package apple.utilities.threading.service.queue;

public class AsyncTaskQueue {
    private static final AsyncTaskQueue instance = new AsyncTaskQueue();

    public static AsyncTaskQueue get() {
        return instance;
    }
}
