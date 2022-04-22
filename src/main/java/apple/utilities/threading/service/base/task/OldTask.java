package apple.utilities.threading.service.base.task;

public class OldTask<T> {
    private final T returnVal;
    private final long timeCompleted = System.currentTimeMillis();

    public OldTask(T returnVal) {
        this.returnVal = returnVal;
    }

    public long getTime() {
        return this.timeCompleted;
    }
}
