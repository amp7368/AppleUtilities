package apple.utilities.database.concurrent;

import apple.utilities.database.concurrent.inst.ConcurrentAJDInst;
import apple.utilities.database.concurrent.inst.ConcurrentAJDInstImpl;
import apple.utilities.database.concurrent.serialize.ConcurrentAJDSerializing;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface ConcurrentAJD<DBType> {

    static <T> ConcurrentAJDInst<T> createInst(Class<T> dbType, File file, Executor executor) {
        return new ConcurrentAJDInstImpl<>(dbType, file, executor);
    }

    static <T> ConcurrentAJDInst<T> createInst(Class<T> dbType, File file) {
        return createInst(dbType, file, Executors.newCachedThreadPool());
    }

    ConcurrentAJDSerializing serializing();
}
