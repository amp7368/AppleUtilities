package apple.utilities.database.ajd;

import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;

public interface AppleAJDInst<DBType> extends AppleAJD<DBType> {

    void set(DBType newThing);

    boolean saveNow();

    AsyncTaskAttempt<Placeholder, ?> save();

    AsyncTaskAttempt<DBType, ?> load();

    DBType loadNow();

    default DBType loadOrMake() {
        return loadOrMake(false);
    }

    DBType loadOrMake(boolean safeMode);

    DBType getInstance();
}
