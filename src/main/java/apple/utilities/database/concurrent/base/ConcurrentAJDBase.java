package apple.utilities.database.concurrent.base;

import apple.utilities.database.concurrent.serialize.ConcurrentAJDSerializing;

public interface ConcurrentAJDBase<DBType> {

    ConcurrentAJDSerializing serializing();
}
