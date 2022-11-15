package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;
import java.io.File;
import java.util.Collection;

public class AppleAJDTypedImpl<DBType extends SaveFileable, TaskExtra> extends AppleAJD implements
    AppleAJDTyped<DBType, TaskExtra> {

    protected final Class<DBType> dbType;
    protected final File folder;
    protected final AsyncTaskQueueStart<TaskExtra> queue;

    public AppleAJDTypedImpl(Class<DBType> dbType, File folder,
        AsyncTaskQueueStart<TaskExtra> queue) {
        this.dbType = dbType;
        this.folder = folder;
        this.queue = queue;
    }

    public AppleAJDTypedImpl(Class<DBType> dbType, File folder,
        AsyncTaskQueueStart<TaskExtra> queue, Gson gson) {
        super(gson);
        this.dbType = dbType;
        this.folder = folder;
        this.queue = queue;
    }


    @Override
    public boolean delete(DBType deleteThis) {
        return FileFormatting.fileWithChildren(this.folder, deleteThis.getSaveFilePath()).delete();
    }

    @Override
    public void saveInFolderNow(DBType saveThis) {
        this.saveInFolder(saveThis).complete();
    }

    @Override
    public AsyncTaskAttempt<Placeholder, TaskExtra> saveInFolder(DBType saveThis) {
        return this.saveInFolder(this.folder, saveThis, this.queue);
    }

    @Override
    public Collection<AsyncTaskAttempt<DBType, TaskExtra>> loadFolder() {
        return this.loadFolder(this.folder, this.dbType, this.queue);
    }

    @Override
    public AsyncTaskAttempt<DBType, TaskExtra> loadFromFolder(File file) {
        return this.load(new File(this.folder, file.getPath()), this.dbType, this.queue);
    }

    @Override
    public DBType loadFromFolderNow(File file) {
        return this.loadFromFolder(file).complete();
    }

    @Override
    public AsyncTaskAttempt<DBType, TaskExtra> loadFromFolder(String... children) {
        return this.load(FileFormatting.fileWithChildren(this.folder, children), this.dbType,
            this.queue);
    }

    @Override
    public DBType loadFromFolderNow(String... children) {
        return this.loadFromFolder(children).complete();
    }

    @Override
    public Collection<DBType> loadFolderNow() {
        return this.loadFolderNow(this.folder, this.dbType, this.queue);
    }
}
