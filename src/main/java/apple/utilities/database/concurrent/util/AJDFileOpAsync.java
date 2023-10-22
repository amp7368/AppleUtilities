package apple.utilities.database.concurrent.util;

import java.util.concurrent.Executor;
import java.util.function.Function;

public class AJDFileOpAsync extends AJDFileOpAsyncBase<String, Boolean> {

    public AJDFileOpAsync(Function<String, Boolean> asyncOp, Executor executor) {
        super(asyncOp, executor);
    }
}
