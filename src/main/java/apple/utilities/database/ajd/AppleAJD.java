package apple.utilities.database.ajd;

import apple.file.yml.BaseYcm;
import apple.utilities.database.HasFilename;
import apple.utilities.database.ajd.impl.AppleAJDBase;
import apple.utilities.database.ajd.impl.AppleAJDInstImpl;
import apple.utilities.database.ajd.impl.AppleAJDTypedImpl;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import com.google.gson.Gson;
import java.io.File;

public interface AppleAJD<DBType> {

    static <T> AppleAJDInst<T> createInst(Class<T> dbType, File file, AsyncTaskQueueStart<?> queue) {
        return new AppleAJDInstImpl<>(dbType, file, queue);
    }

    static <T> AppleAJDInst<T> createInst(Class<T> dbType, File file, AsyncTaskQueueStart<?> queue, Gson gson) {
        AppleAJDInstImpl<T> ajd = new AppleAJDInstImpl<>(dbType, file, queue);
        ajd.setSerializingJson(gson);
        return ajd;
    }

    static <T extends HasFilename> AppleAJDTyped<T> createTyped(Class<T> dbType, File folder,
        AsyncTaskQueueStart<?> queue) {
        return new AppleAJDTypedImpl<>(dbType, folder, queue);
    }

    static <T extends HasFilename> AppleAJDTyped<T> createTyped(Class<T> dbType, File folder,
        AsyncTaskQueueStart<?> queue, Gson gson) {
        AppleAJDTypedImpl<T> ajd = new AppleAJDTypedImpl<>(dbType, folder, queue);
        ajd.setSerializingJson(gson);
        return ajd;
    }

    void setSerializing(AppleAJDBase.AppleAJDSerializer serializer, AppleAJDBase.AppleAJDDeserializer deserializer);

    default void setSerializingJson() {
        this.setSerializingJson(null);
    }

    void setSerializingJson(Gson gson);

    default void setSerializingYaml() {
        this.setSerializingYaml(null);
    }

    void setSerializingYaml(BaseYcm ycm);
}
