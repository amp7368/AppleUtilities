package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileable;
import apple.utilities.database.queue.AppleJsonDatabaseLoader;
import apple.utilities.request.AppleRequestQueue;

public interface AppleJsonDatabaseLoaderKey<DBType extends SaveFileable> extends AppleJsonDatabaseLoader<DBType> {
    @Override
    default AppleRequestQueue getIOService() {
        return getIOLoadingService();
    }

    AppleRequestQueue getIOLoadingService();
}
