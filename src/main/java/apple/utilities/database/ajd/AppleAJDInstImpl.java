package apple.utilities.database.ajd;

import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class AppleAJDInstImpl<DBType, TaskExtra> extends AppleAJD implements AppleAJDInst<DBType, TaskExtra> {
    protected final Class<DBType> dbType;
    protected final File file;
    protected final AsyncTaskQueueStart<TaskExtra> queue;
    protected DBType thing = null;

    public AppleAJDInstImpl(Class<DBType> dbType, File file, AsyncTaskQueueStart<TaskExtra> queue) {
        this.dbType = dbType;
        this.file = file;
        this.queue = queue;
    }

    @Override
    public void set(DBType newThing) {
        this.thing = newThing;
    }

    @Override
    public void saveNow() {
        this.save().complete();
    }

    @Override
    public AsyncTaskAttempt<Placeholder, TaskExtra> save() {
        return this.save(this.file, this.thing, this.queue);
    }

    @Override
    public AsyncTaskAttempt<DBType, TaskExtra> load() {
        AsyncTaskAttempt<DBType, TaskExtra> load = this.load(this.file, this.dbType, this.queue);
        load.onSuccess((newThing) -> this.thing = newThing);
        return load;
    }

    @Override
    public DBType loadNow() {
        return this.load().complete();
    }

    @Override
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

    @Override
    public DBType getInstance() {
        return this.thing;
    }
}
