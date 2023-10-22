package apple.utilities.threading.service.priority;

public enum TaskPriorityCommon implements TaskPriority {
    HIGHEST(-3),
    HIGHER(-2),
    HIGH(-1),
    NORMAL(0),
    LOW(1),
    LOWER(2),
    LOWEST(3);

    private final int priority;

    TaskPriorityCommon(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
