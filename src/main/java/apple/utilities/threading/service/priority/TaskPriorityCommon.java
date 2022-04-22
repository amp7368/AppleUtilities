package apple.utilities.threading.service.priority;

public enum TaskPriorityCommon {
    HIGHEST(0),
    HIGHER(1),
    HIGH(2),
    NORMAL(3),
    LOW(4),
    LOWER(5),
    LOWEST(6);

    private final int priority;

    TaskPriorityCommon(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
