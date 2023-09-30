package apple.utilities.database.concurrent.group;

import apple.utilities.database.concurrent.base.ConcurrentAJDBaseImpl;
import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ConcurrentAJDTypedImpl<DBType> extends ConcurrentAJDBaseImpl<DBType> implements ConcurrentAJDTyped<DBType> {

    @Override
    public boolean delete(DBType deleteThis) {
        return false;
    }

    @Override
    public boolean saveInFolderNow(DBType saveThis) {
        return false;
    }

    @Override
    public CompletableFuture<Boolean> saveInFolder(DBType saveThis) {
        return null;
    }

    @Override
    public CompletableFuture<Collection<DBType>> loadFolder() {
        return null;
    }

    @Override
    public CompletableFuture<Collection<DBType>> loadFolder(boolean safeMode) {
        return null;
    }

    @Override
    public CompletableFuture<DBType> loadOne(File file) {
        return null;
    }

    @Override
    public CompletableFuture<DBType> loadOne(String... children) {
        return null;
    }

    @Override
    public DBType loadOneNow(File file) {
        return null;
    }

    @Override
    public DBType loadOneNow(String... children) {
        return null;
    }
}
