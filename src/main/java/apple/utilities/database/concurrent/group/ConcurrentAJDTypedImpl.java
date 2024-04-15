package apple.utilities.database.concurrent.group;

import apple.utilities.database.HasFilename;
import apple.utilities.database.concurrent.base.ConcurrentAJDBaseImpl;
import apple.utilities.database.concurrent.util.AJDFileOpAsync;
import apple.utilities.util.FileFormatting;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class ConcurrentAJDTypedImpl<DBType extends HasFilename> extends ConcurrentAJDBaseImpl<DBType> implements
    ConcurrentAJDTyped<DBType> {

    protected final File folder;

    protected final Map<String, AJDFileOpAsync> saveOp = new HashMap<>();

    public ConcurrentAJDTypedImpl(Class<DBType> dbType, File folder, Executor executor) {
        super(dbType, executor);
        this.folder = folder;
    }

    @NotNull
    private AJDFileOpAsync computeTakeLastOp(DBType saveThis) {
        return computeTakeLastOp(saveThis.fileWithParent(folder));
    }

    @NotNull
    private AJDFileOpAsync computeTakeLastOp(File file) {
        String key = file.getAbsolutePath();
        return saveOp.computeIfAbsent(key, k -> new AJDFileOpAsync(s -> writeToFile(file, s), executor));
    }

    @Override
    public boolean delete(DBType deleteThis) {
        File file = deleteThis.fileWithParent(folder);
        return computeTakeLastOp(file).stop(file::delete);
    }

    @Override
    public boolean saveInFolderNow(DBType saveThis) {
        String serialized = serializing.serialize(saveThis);
        return computeTakeLastOp(saveThis).completeNow(serialized);
    }

    @Override
    public CompletableFuture<Boolean> saveInFolder(DBType saveThis) {
        String serialized = serializing.serialize(saveThis);
        return computeTakeLastOp(saveThis).tryStart(serialized);
    }

    private void saveToFile(File file, DBType saveThis) {
        String serialized = serializing.serialize(saveThis);
        computeTakeLastOp(file).tryStart(serialized);
    }

    @Override
    public CompletableFuture<Collection<DBType>> loadFolder(boolean safeMode) {
        File[] files = folder.listFiles();
        if (files == null) return CompletableFuture.completedFuture(Collections.emptyList());
        List<CompletableFuture<DBType>> loadFolder = Arrays.stream(files).map((file) -> this.loadOne(safeMode, file)).toList();
        CompletableFuture<?>[] futures = loadFolder.toArray(CompletableFuture<?>[]::new);
        return CompletableFuture.allOf(futures)
            .thenApply(ignored -> loadFolder
                .stream()
                .map(future -> future.getNow(null))
                .filter(Objects::nonNull)
                .toList()
            );
    }

    @Override
    public CompletableFuture<DBType> loadOne(boolean safeMode, File file) {
        return CompletableFuture.supplyAsync(() -> this.loadOneNow(safeMode, file), executor);
    }

    @Override
    public CompletableFuture<DBType> loadOne(boolean safeMode, String... children) {
        File file = FileFormatting.fileWithChildren(this.folder, children);
        return loadOne(safeMode, file);
    }

    @Override
    public DBType loadOneNow(boolean safeMode, File file) {
        try {
            DBType inst = uncheckedLoadNow(file, safeMode);
            this.saveToFile(file, inst);
            return inst;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public DBType loadOneNow(boolean safeMode, String... children) {
        File file = FileFormatting.fileWithChildren(this.folder, children);
        return loadOneNow(safeMode, file);
    }
}
