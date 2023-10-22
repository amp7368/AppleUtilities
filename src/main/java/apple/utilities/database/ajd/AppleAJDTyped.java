package apple.utilities.database.ajd;

import apple.utilities.database.HasFilename;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import java.io.File;
import java.util.Collection;

public interface AppleAJDTyped<DBType extends HasFilename> extends AppleAJD<DBType> {

    boolean delete(DBType deleteThis);

    boolean saveInFolderNow(DBType saveThis);

    AsyncTaskAttempt<Placeholder, ?> saveInFolder(DBType saveThis);

    Collection<AsyncTaskAttempt<DBType, ?>> loadFolder();

    default Collection<DBType> loadFolderNow() {
        return loadFolderNow(false);
    }

    Collection<DBType> loadFolderNow(boolean safeMode);

    AsyncTaskAttempt<DBType, ?> loadFromFolder(File file);

    AsyncTaskAttempt<DBType, ?> loadFromFolder(String... children);

    DBType loadFromFolderNow(File file);

    DBType loadFromFolderNow(String... children);

}
