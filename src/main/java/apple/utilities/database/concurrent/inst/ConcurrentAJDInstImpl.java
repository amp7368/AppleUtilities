package apple.utilities.database.concurrent.inst;

import apple.utilities.database.concurrent.base.ConcurrentAJDBaseImpl;
import apple.utilities.database.concurrent.util.AJDFileOpAsync;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConcurrentAJDInstImpl<DBType> extends ConcurrentAJDBaseImpl<DBType> implements ConcurrentAJDInst<DBType> {

    protected final File file;
    protected DBType thing = null;
    private final AJDFileOpAsync saveOp;

    public ConcurrentAJDInstImpl(Class<DBType> dbType, File file, Executor executor) {
        super(dbType, executor);
        this.file = file;
        this.saveOp = new AJDFileOpAsync(this::writeToFile, executor);
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
        return this.saveOp.completeNow(serializing.serialize(this.thing));
    }

    @Override
    public CompletableFuture<Boolean> save() {
        String serialized = serializing.serialize(this.thing);
        return this.saveOp.tryStart(serialized);
    }

    @Override
    public CompletableFuture<DBType> load() {
        return CompletableFuture.supplyAsync(this::loadNow, executor);
    }

    @Override
    @NotNull
    public DBType uncheckedLoadNow(boolean safeMode) throws IOException {
        this.setValue(uncheckedLoadNow(this.file, safeMode));
        this.save();
        return this.thing;
    }

    @Override
    @Nullable
    public DBType loadNow(boolean safeMode) {
        try {
            this.uncheckedLoadNow(safeMode);
        } catch (Exception e) {
            System.err.println("Error loading " + this.dbType.getName());
            e.printStackTrace();
            return null;
        }
        return this.thing;
    }

    @NotNull
    private Boolean writeToFile(String serialized) {
        return writeToFile(this.file, serialized);
    }
}
