package apple.utilities.database.concurrent;

import apple.utilities.database.HasFilename;
import apple.utilities.database.concurrent.group.ConcurrentAJDTyped;
import apple.utilities.database.concurrent.group.ConcurrentAJDTypedImpl;
import apple.utilities.database.concurrent.inst.ConcurrentAJDInst;
import apple.utilities.database.concurrent.inst.ConcurrentAJDInstImpl;
import com.google.gson.Gson;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.Nullable;

public interface ConcurrentAJD {

    static <T> ConcurrentAJDInst<T> createInst(Class<T> dbType, File file) {
        return createInst(dbType, file, null, Executors.newCachedThreadPool());
    }

    static <T> ConcurrentAJDInst<T> createInst(Class<T> dbType, File file, Executor executor) {
        return createInst(dbType, file, null, executor);
    }

    static <T> ConcurrentAJDInst<T> createInst(Class<T> dbType, File file, Gson gson) {
        return createInst(dbType, file, gson, Executors.newCachedThreadPool());
    }

    static <T> ConcurrentAJDInst<T> createInst(Class<T> dbType, File file, @Nullable Gson gson, Executor executor) {
        ConcurrentAJDInstImpl<T> db = new ConcurrentAJDInstImpl<>(dbType, file, executor);
        if (gson != null) db.serializing().asGson(gson);
        return db;
    }

    static <T extends HasFilename> ConcurrentAJDTyped<T> createTyped(Class<T> dbType, File folder) {
        return createTyped(dbType, folder, null, Executors.newCachedThreadPool());
    }

    static <T extends HasFilename> ConcurrentAJDTyped<T> createTyped(Class<T> dbType, File folder, ExecutorService executor) {
        return createTyped(dbType, folder, null, executor);
    }

    static <T extends HasFilename> ConcurrentAJDTyped<T> createTyped(Class<T> dbType, File folder, Gson gson) {
        return createTyped(dbType, folder, gson, Executors.newCachedThreadPool());
    }

    static <T extends HasFilename> ConcurrentAJDTyped<T> createTyped(Class<T> dbType, File folder, Gson gson,
        ExecutorService executor) {
        ConcurrentAJDTypedImpl<T> db = new ConcurrentAJDTypedImpl<>(dbType, folder, executor);
        if (gson != null) db.serializing().asGson(gson);
        return db;
    }


}
