package apple.utilities.threading.service.priority;

public class AsyncTaskPriority {

    private final int priority;

    public AsyncTaskPriority(int priority) {
        this.priority = priority;
    }

    public AsyncTaskPriority(TaskPriority priority) {
        this.priority = priority.getPriority();
    }

    public int getPriority() {
        return priority;
    }
}
