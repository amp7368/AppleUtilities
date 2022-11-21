package apple.utilities.database.ajd.impl;

import apple.utilities.database.ajd.AppleAJDInst;
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
        save();
        return this.thing;
    }

    @Override
    public DBType loadOrMake() {
        try {
            this.thing = merge(this.deserializer().get());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading " + this.dbType.getName());
        }
        save();
        return this.thing;
    }

    @Override
    public DBType makeNew() {
        return super.makeNew();
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
