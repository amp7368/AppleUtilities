package apple.utilities.database.concurrent.base;

import apple.utilities.database.concurrent.serialize.ConcurrentAJDSerializing;
import apple.utilities.database.util.ReflectionsUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executor;

public class ConcurrentAJDBaseImpl<DBType> implements ConcurrentAJDBase<DBType> {

    protected final Executor executor;
    protected final Class<DBType> dbType;

    protected ConcurrentAJDSerializing serializing;

    public ConcurrentAJDBaseImpl(Class<DBType> dbType, Executor executor) {
        this.dbType = dbType;
        this.executor = executor;
        this.serializing = new ConcurrentAJDSerializing();
    }

    protected static Boolean writeToFile(File file, String serialized) {
        try {
            Files.writeString(file.toPath(), serialized,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected DBType uncheckedLoadNow(File file, boolean safeMode) throws IOException {
        DBType obj = serializing().deserialize(file, this.dbType);
        if (!safeMode)
            return ReflectionsUtil.merge(dbType, obj);
        else if (obj == null)
            return ReflectionsUtil.makeNew(this.dbType);
        return obj;
    }

    // modify 'initial', discard 'loaded'

    @Override
    public ConcurrentAJDSerializing serializing() {
        return this.serializing;
    }
}
