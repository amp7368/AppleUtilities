package apple.utilities.database.ajd;

import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class AppleAJDInstImpl<DBType, TaskExtra> extends AppleAJD {
    protected final Class<DBType> dbType;
    protected final File file;
    protected final AsyncTaskQueueStart<TaskExtra> queue;
    protected DBType thing = null;

    public AppleAJDInstImpl(Class<DBType> dbType, File file, AsyncTaskQueueStart<TaskExtra> queue) {
        this.dbType = dbType;
        this.file = file;
        this.queue = queue;
    }

    public void set(DBType newThing) {
        this.thing = newThing;
    }

    public void saveNow() {
        this.save().complete();
    }

    public AsyncTaskAttempt<Placeholder, TaskExtra> save() {
        return this.save(this.file, this.thing, this.queue);
    }

    public AsyncTaskAttempt<DBType, TaskExtra> load() {
        AsyncTaskAttempt<DBType, TaskExtra> load = this.load(this.file, this.dbType, this.queue);
        load.onSuccess((newThing) -> this.thing = newThing);
        return load;
    }

    public DBType loadNow() {
        return this.load().complete();
    }

    public DBType loadOrMake() {
        this.thing = this.loadNow();
        if (this.thing == null) makeNew();
        return this.thing;
    }

    private void makeNew() {
        try {
            this.thing = this.dbType.getConstructor().newInstance();
            this.save();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("There is not a no-args constructor in " + this.dbType.getName(), e);
        }
    }

    public DBType getInstance() {
        return this.thing;
    }
}
