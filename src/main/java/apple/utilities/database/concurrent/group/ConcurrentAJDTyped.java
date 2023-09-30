package apple.utilities.database.concurrent.group;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ConcurrentAJDTyped<DBType> {

    boolean delete(DBType deleteThis);

    // save
    boolean saveInFolderNow(DBType saveThis);

    CompletableFuture<Boolean> saveInFolder(DBType saveThis);

    // loadFolder
    CompletableFuture<Collection<DBType>> loadFolder();

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
    CompletableFuture<DBType> loadOne(File file);

    CompletableFuture<DBType> loadOne(String... children);

    DBType loadOneNow(File file);

    DBType loadOneNow(String... children);

}
