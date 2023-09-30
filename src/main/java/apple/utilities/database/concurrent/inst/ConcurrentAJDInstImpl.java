package apple.utilities.database.concurrent.inst;

import apple.utilities.database.concurrent.base.ConcurrentAJDBaseImpl;
import apple.utilities.database.concurrent.util.TakeLastOpAsync;
import apple.utilities.database.util.ReflectionsUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcurrentAJDInstImpl<DBType> extends ConcurrentAJDBaseImpl<DBType> implements ConcurrentAJDInst<DBType> {

    protected final File file;
    private final TakeLastOpAsync<String, Boolean> saveOp;
    protected DBType thing = null;

    public ConcurrentAJDInstImpl(Class<DBType> dbType, File file, Executor executor) {
        super(dbType, executor);
        this.file = file;
        this.saveOp = new TakeLastOpAsync<>(this::writeToFile, executor);
    }

    @Override
    public DBType getValue() {
        return this.thing;
    }

    @Override
    public ConcurrentAJDInstImpl<DBType> setValue(DBType newThing) {
        this.thing = newThing;
        return this;
    }

    @Override
    public boolean saveNow() {
        String serialized = serializing.serialize(this.thing);
        return this.saveOp.completeNow(serialized);
    }

    @Override
    public Future<Boolean> save() {
        String serialized = serializing.serialize(this.thing);
        return this.saveOp.tryStart(serialized);
    }

    @Override
    public Future<DBType> load() {
        return CompletableFuture.supplyAsync(this::loadNow, executor);
    }

    @Override
    @NotNull
    public DBType uncheckedLoadNow(boolean safeMode) throws IOException {
        this.setValue(serializing().deserialize(this.file, this.dbType));
        if (!safeMode)
            this.setValue(mergeOntoNew(this.thing));
        else if (this.thing == null)
            this.setValue(ReflectionsUtil.makeNew(this.dbType));
        this.save();
        return this.thing;
    }

    @Override
    @Nullable
    public DBType loadNow(boolean safeMode) {
        try {
            this.setValue(uncheckedLoadNow());
        } catch (Exception e) {
            System.err.println("Error loading " + this.dbType.getName());
            e.printStackTrace();
            return null;
        }
        return this.thing;
    }

    @NotNull
    private Boolean writeToFile(String serialized) {
        try {
            Files.writeString(file.toPath(), serialized, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
