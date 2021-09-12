package apple.utilities.event;

public interface AppleListenerPriority {
    default int compare(AppleListenerPriority other) {
        return ordinal() - other.ordinal();
    }

    int ordinal();
}
