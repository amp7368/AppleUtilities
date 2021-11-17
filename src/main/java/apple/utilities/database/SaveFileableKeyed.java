package apple.utilities.database;

public interface SaveFileableKeyed extends SaveFileable {
    default Object getSaveId() {
        return getSaveFileName();
    }
}
