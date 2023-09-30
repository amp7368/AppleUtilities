package apple.utilities.database.concurrent.inst;

import java.io.IOException;
import java.util.concurrent.Future;

public interface ConcurrentAJDInst<DBType> {

    DBType getValue();

    ConcurrentAJDInst<DBType> setValue(DBType newThing);

    boolean saveNow();

    Future<Boolean> save();

    Future<DBType> load();

    default DBType uncheckedLoadNow() throws IOException {
        return uncheckedLoadNow(false);
    }

    DBType uncheckedLoadNow(boolean safeMode) throws IOException;

    default DBType loadNow() {
        return loadNow(false);
    }

    DBType loadNow(boolean safeMode);

}
