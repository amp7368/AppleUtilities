package apple.utilities.database;

import java.io.File;

public interface SaveFileable {
    String getSaveFileName();

    default File getSaveFile() {
        return new File(getSaveFileName());
    }
}
