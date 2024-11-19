package apple.utilities.database;

@Deprecated
public interface SaveFileableKeyed extends SaveFileable {

    default Object getSaveId() {
        return getSaveFileName();
    }
}
