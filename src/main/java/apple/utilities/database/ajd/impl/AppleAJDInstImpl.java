package apple.utilities.database.ajd.impl;

import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.database.util.ReflectionsUtil;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import java.io.File;

public class AppleAJDInstImpl<DBType> extends AppleAJDBase<DBType> implements AppleAJDInst<DBType> {

    protected final File file;
    protected DBType thing = null;

    public AppleAJDInstImpl(Class<DBType> dbType, File file, AsyncTaskQueueStart<?> queue) {
        super(dbType, queue);
        this.file = file;
    }

    @Override
    public void set(DBType newThing) {
        this.thing = newThing;
    }

    @Override
    public AsyncTaskAttempt<Placeholder, ?> save() {
        return this.queue.accept(this.serializer());
    }

    @Override
    public boolean saveNow() {
        try {
            this.serializer().get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AsyncTaskAttempt<DBType, ?> load() {
        AsyncTaskAttempt<DBType, ?> load = queue.accept(this.deserializer());
        load.onSuccess(this::set);
        return load;
    }

    @Override
    public DBType loadNow() {
        try {
            this.thing = this.deserializer().get();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading " + this.dbType.getName());
        }
        saveNow();
        return this.thing;
    }

    @Override
    public DBType loadOrMake(boolean safeMode) {
        try {
            this.thing = deserializer().get();
            if (!safeMode)
                this.thing = merge(this.thing);
            else if (this.thing == null) this.thing = ReflectionsUtil.makeNew(this.dbType);
        } catch (Exception e) {
            System.err.println("Error loading " + this.dbType.getName());
            e.printStackTrace();
        }
        saveNow();
        return this.thing;
    }

    private SupplierUncaught<Placeholder> serializer() {
        return this.serializing.serializer(this.file, this.thing);
    }

    private SupplierUncaught<DBType> deserializer() {
        return this.serializing.deserializer(this.file, this.dbType);
    }


    @Override
    public DBType getInstance() {
        return this.thing;
    }
}
