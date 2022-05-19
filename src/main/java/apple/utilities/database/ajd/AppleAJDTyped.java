package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;

import java.io.File;
import java.util.Collection;

public interface AppleAJDTyped<DBType extends SaveFileable, TaskExtra> {
    void saveInFolderNow(DBType saveThis);

    AsyncTaskAttempt<Placeholder, TaskExtra> saveInFolder(DBType saveThis);

    Collection<AsyncTaskAttempt<DBType, TaskExtra>> loadFolder();

    AsyncTaskAttempt<DBType, TaskExtra> loadFromFolder(File file);

    DBType loadFromFolderNow(File file);

    AsyncTaskAttempt<DBType, TaskExtra> loadFromFolder(String... children);

    DBType loadFromFolderNow(String... children);

    Collection<DBType> loadFolderNow();
}
