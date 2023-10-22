package apple.utilities.database.ajd.impl;

import apple.utilities.database.HasFilename;
import apple.utilities.database.ajd.AppleAJDTyped;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import apple.utilities.util.FileFormatting;
import java.io.File;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AppleAJDTypedImpl<DBType extends HasFilename> extends AppleAJDBase<DBType> implements AppleAJDTyped<DBType> {

    protected final File folder;

    public AppleAJDTypedImpl(Class<DBType> dbType, File folder, AsyncTaskQueueStart<?> queue) {
        super(dbType, queue);
        this.folder = folder;
    }


    @Override
    public boolean delete(DBType deleteThis) {
        return deleteThis.fileWithParent(this.folder).delete();
    }

    @Override
    public AsyncTaskAttempt<Placeholder, ?> saveInFolder(DBType saveThis) {
        return this.queue.accept(this.serializer(saveThis));
    }

    @Override
    public boolean saveInFolderNow(DBType saveThis) {
        try {
            this.serializer(saveThis).get();
            return true;
        } catch (Exception e) {
            logError(e);
            return false;
        }
    }

    public Collection<AsyncTaskAttempt<DBType, ?>> loadFolder() {
        File[] files = folder.listFiles();
        if (files == null)
            return Collections.emptyList();
        Collection<AsyncTaskAttempt<DBType, ?>> tasks = new ArrayList<>(files.length);
        for (File file : files)
            tasks.add(queue.accept(deserializer(file)));
        return tasks;
    }

    public Collection<DBType> loadFolderNow(boolean safeMode) {
        File[] files = folder.listFiles();
        if (files == null)
            return Collections.emptyList();
        Collection<DBType> dbs = new ArrayList<>(files.length);
        for (File file : files) {
            DBType out = null;
            try {
                out = deserializer(file).get();
            } catch (Exception e) {
                logError(e);
            }
            if (safeMode) dbs.add(out);
            else {
                try {
                    dbs.add(merge(out));
                } catch (AccessException e) {
                    logError(e);
                }
            }
        }
        return dbs;
    }

    private void logError(Exception e) {
        e.printStackTrace();
        System.err.println("Error loading " + this.dbType.getName());
    }

    public AsyncTaskAttempt<DBType, ?> loadFromFolder(File children) {
        File file = new File(this.folder, children.getPath());
        return queue.accept(deserializer(file));
    }

    public AsyncTaskAttempt<DBType, ?> loadFromFolder(String... children) {
        File file = FileFormatting.fileWithChildren(this.folder, children);
        return queue.accept(deserializer(file));
    }

    public DBType loadFromFolderNow(File children) {
        File file = new File(this.folder, children.getPath());
        try {
            return deserializer(file).get();
        } catch (Exception e) {
            logError(e);
            return null;
        }
    }

    public DBType loadFromFolderNow(String... children) {
        File file = FileFormatting.fileWithChildren(this.folder, children);
        try {
            return deserializer(file).get();
        } catch (Exception e) {
            logError(e);
            return null;
        }
    }

    private SupplierUncaught<Placeholder> serializer(DBType saveThis) {
        return this.serializing.serializer(saveThis.fileWithParent(this.folder), saveThis);
    }

    private SupplierUncaught<DBType> deserializer(File file) {
        return this.serializing.deserializer(file, this.dbType);
    }

}
