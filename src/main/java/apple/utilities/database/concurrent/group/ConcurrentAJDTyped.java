package apple.utilities.database.concurrent.group;

import apple.utilities.database.HasFilename;
import apple.utilities.database.concurrent.base.ConcurrentAJDBase;
import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ConcurrentAJDTyped<DBType extends HasFilename> extends ConcurrentAJDBase<DBType> {

    boolean delete(DBType deleteThis);

    // save
    boolean saveInFolderNow(DBType saveThis);

    CompletableFuture<Boolean> saveInFolder(DBType saveThis);

    // loadFolder
    default CompletableFuture<Collection<DBType>> loadFolder() {
        return loadFolder(false);
    }

    CompletableFuture<Collection<DBType>> loadFolder(boolean safeMode);

    default Collection<DBType> loadFolderNow() {
        return loadFolderNow(false);
    }

    default Collection<DBType> loadFolderNow(boolean safeMode) {
        try {
            return loadFolder(safeMode).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    // loadOne
    default CompletableFuture<DBType> loadOne(File file) {
        return loadOne(false, file);
    }

    CompletableFuture<DBType> loadOne(boolean safeMode, File file);

    default CompletableFuture<DBType> loadOne(String... children) {
        return loadOne(false, children);
    }

    CompletableFuture<DBType> loadOne(boolean safeMode, String... children);

    default DBType loadOneNow(File file) {
        return loadOneNow(false, file);
    }

    DBType loadOneNow(boolean safeMode, File file);

    default DBType loadOneNow(String... children) {
        return loadOneNow(false, children);
    }

    DBType loadOneNow(boolean safeMode, String... children);

}
