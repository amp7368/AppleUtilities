package apple.utilities.threading.service.priority;

public class AsyncTaskPriority {
    private int priority;

    public AsyncTaskPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
