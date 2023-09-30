package apple.utilities.database.concurrent.base;

import apple.utilities.database.concurrent.ConcurrentAJD;
import apple.utilities.database.concurrent.serialize.ConcurrentAJDSerializing;
import apple.utilities.database.util.ReflectionsUtil;
import java.rmi.AccessException;
import java.util.concurrent.Executor;

public class ConcurrentAJDBaseImpl<DBType> implements ConcurrentAJD<DBType> {

    protected final Executor executor;
    protected final Class<DBType> dbType;

    protected ConcurrentAJDSerializing serializing;

    public ConcurrentAJDBaseImpl(Class<DBType> dbType, Executor executor) {
        this.dbType = dbType;
        this.executor = executor;
        this.serializing = new ConcurrentAJDSerializing();
    }

    // modify 'initial', discard 'loaded'
    protected DBType mergeOntoNew(DBType loaded) throws AccessException {
        return ReflectionsUtil.merge(dbType, loaded);
    }

    @Override
    public ConcurrentAJDSerializing serializing() {
        return this.serializing;
    }
}
