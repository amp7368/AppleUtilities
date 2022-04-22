package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;

import java.io.File;
import java.util.Collection;

public class AppleAJDTypedImpl<DBType extends SaveFileable, TaskExtra> extends AppleAJD {
    protected final Class<DBType> dbType;
    protected final File folder;
    protected final AsyncTaskQueueStart<TaskExtra> queue;

    public AppleAJDTypedImpl(Class<DBType> dbType, File folder, AsyncTaskQueueStart<TaskExtra> queue) {
        this.dbType = dbType;
        this.folder = folder;
        this.queue = queue;
    }

    public void saveInFolderNow(DBType saveThis) {
        this.saveInFolder(saveThis).complete();
    }

    public AsyncTaskAttempt<Placeholder, TaskExtra> saveInFolder(DBType saveThis) {
        return this.saveInFolder(this.folder, saveThis, this.queue);
    }

    public Collection<AsyncTaskAttempt<DBType, TaskExtra>> loadFolder() {
        return this.loadFolder(this.folder, this.dbType, this.queue);
    }

    public Collection<DBType> loadFolderNow() {
        return this.loadFolderNow(this.folder, this.dbType, this.queue);
    }
}
