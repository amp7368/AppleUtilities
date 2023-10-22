package apple.utilities.database.concurrent.inst;

import apple.utilities.database.concurrent.base.ConcurrentAJDBase;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface ConcurrentAJDInst<DBType> extends ConcurrentAJDBase<DBType> {

    DBType getValue();

    ConcurrentAJDInst<DBType> setValue(DBType newThing);

    boolean saveNow();

    CompletableFuture<Boolean> save();

    CompletableFuture<DBType> load();

    default DBType uncheckedLoadNow() throws IOException {
        return uncheckedLoadNow(false);
    }

    DBType uncheckedLoadNow(boolean safeMode) throws IOException;

    default DBType loadNow() {
        return loadNow(false);
    }

    DBType loadNow(boolean safeMode);

}
