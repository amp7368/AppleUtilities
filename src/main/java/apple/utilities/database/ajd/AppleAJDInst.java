package apple.utilities.database.ajd;

import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;

public interface AppleAJDInst<DBType, TaskExtra> {
    void set(DBType newThing);

    void saveNow();

    AsyncTaskAttempt<Placeholder, TaskExtra> save();

    AsyncTaskAttempt<DBType, TaskExtra> load();

    DBType loadNow();

    DBType loadOrMake();

    DBType getInstance();
}
