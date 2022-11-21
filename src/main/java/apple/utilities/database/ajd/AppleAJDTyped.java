package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import java.io.File;
import java.util.Collection;

public interface AppleAJDTyped<DBType extends SaveFileable> extends AppleAJD<DBType> {

    boolean delete(DBType deleteThis);

    boolean saveInFolderNow(DBType saveThis);

    AsyncTaskAttempt<Placeholder, ?> saveInFolder(DBType saveThis);

    Collection<AsyncTaskAttempt<DBType, ?>> loadFolder();

    Collection<DBType> loadFolderNow();

    AsyncTaskAttempt<DBType, ?> loadFromFolder(File file);

    AsyncTaskAttempt<DBType, ?> loadFromFolder(String... children);

    DBType loadFromFolderNow(File file);

    DBType loadFromFolderNow(String... children);

}
