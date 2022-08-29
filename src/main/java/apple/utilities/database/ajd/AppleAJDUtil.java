package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.fileio.serializing.FileIOJoined;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.base.task.AsyncTaskAttempt;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public interface AppleAJDUtil extends FileIOJoined {

    default <DBType extends SaveFileable, TaskExtra> Placeholder saveInFolderNow(File folder,
        DBType saveThis, AsyncTaskQueueStart<TaskExtra> queue) {
        return saveInFolder(folder, saveThis, queue).complete();
    }

    default <DBType extends SaveFileable, TaskExtra> AsyncTaskAttempt<Placeholder, TaskExtra> saveInFolder(
        File folder, DBType saveThis, AsyncTaskQueueStart<TaskExtra> queue) {
        return this.save(saveThis.fileWithParent(folder), saveThis, queue);
    }

    default <TaskExtra, DBType> void saveNow(File file, DBType saveThis,
        AsyncTaskQueueStart<TaskExtra> queue) {
        this.save(file, saveThis, queue).complete();
    }

    default <TaskExtra, DBType> AsyncTaskAttempt<Placeholder, TaskExtra> save(File file,
        DBType saveThis, AsyncTaskQueueStart<TaskExtra> queue) {
        file.getParentFile().mkdirs();
        return queue.accept(serializer(file, saveThis));
    }

    default <DBType, TaskExtra> Collection<DBType> loadFolderNow(File folder, Class<DBType> dbType,
        AsyncTaskQueueStart<TaskExtra> queue) {
        return loadFolder(folder, dbType, queue).stream().map(AsyncTaskAttempt::complete).toList();
    }

    default <DBType, TaskExtra> Collection<AsyncTaskAttempt<DBType, TaskExtra>> loadFolder(
        File folder, Class<DBType> dbType, AsyncTaskQueueStart<TaskExtra> queue) {
        File[] files = folder.listFiles();
        if (files == null)
            return Collections.emptyList();
        Collection<AsyncTaskAttempt<DBType, TaskExtra>> dbs = new ArrayList<>(files.length);
        for (File file : files) {
            dbs.add(load(file, dbType, queue));
        }
        return dbs;
    }


    default <DBType, Extra> DBType loadNow(File file, Class<DBType> type,
        AsyncTaskQueueStart<Extra> queue) {
        return this.load(file, type, queue).complete();
    }

    default <DBType, Extra> AsyncTaskAttempt<DBType, Extra> load(File file, Class<DBType> type,
        AsyncTaskQueueStart<Extra> queue) {
        return queue.accept(deserializer(file, type));
    }

    default <DBType> SupplierUncaught<Placeholder> serializer(File file, DBType saveThis) {
        return this.jsonSerializer(file, saveThis);
    }

    default <DBType> SupplierUncaught<DBType> deserializer(File file, Class<DBType> dbType) {
        return this.jsonDeserializer(file, dbType);
    }

    default <DBType> SupplierUncaught<Placeholder> jsonSerializer(File dbFile, DBType saveThis) {
        return () -> {
            this.saveJson(dbFile, saveThis);
            return null;
        };
    }

    default <DBType> SupplierUncaught<Placeholder> jsonSerializer(File dbFile, DBType saveThis,
        Gson gson) {
        return () -> {
            this.saveJson(dbFile, saveThis, gson);
            return null;
        };
    }

    default <DBType> SupplierUncaught<Placeholder> yamlSerializer(File dbFile, DBType saveThis) {
        return () -> {
            this.saveYaml(dbFile, saveThis);
            return null;
        };
    }


    default <DBType> SupplierUncaught<DBType> jsonDeserializer(File file, Class<DBType> type) {
        return () -> {
            try {
                return this.loadJson(file, type);
            } catch (IOException e) {
                return null;
            }
        };
    }

    default <DBType> SupplierUncaught<DBType> jsonDeserializer(File file, Class<DBType> type,
        Gson gson) {
        return () -> {
            try {
                return this.loadJson(file, type, gson);
            } catch (IOException e) {
                return null;
            }
        };
    }

    default <DBType> SupplierUncaught<DBType> yamlDeserializer(File file, Class<DBType> type) {
        return () -> {
            try {
                return this.loadYaml(file, type);
            } catch (IOException e) {
                return null;
            }
        };
    }
}
