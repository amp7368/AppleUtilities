package apple.utilities.database.ajd.util;

import apple.file.yml.BaseYcm;
import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import com.google.gson.Gson;

public interface HasAppleAJDInst<DBType> extends AppleAJDInst<DBType> {

    AppleAJDInst<DBType> get();

    @Override
    default void set(DBType newThing) {
        get().set(newThing);
    }

    default boolean saveNow() {
        return get().saveNow();
    }

    @Override
    default AsyncTaskAttempt<Placeholder, ?> save() {
        return get().save();
    }

    @Override
    default AsyncTaskAttempt<DBType, ?> load() {
        return get().load();
    }

    @Override
    default DBType loadNow() {
        return get().loadNow();
    }

    @Override
    default DBType loadOrMake(boolean safeMode) {
        return get().loadOrMake(safeMode);
    }

    @Override
    default DBType getInstance() {
        return get().getInstance();
    }


    @Override
    default void setSerializingJson(Gson gson) {
        get().setSerializingJson(gson);
    }

    @Override
    default void setSerializingYaml(BaseYcm ycm) {
        get().setSerializingYaml(ycm);
    }
}
