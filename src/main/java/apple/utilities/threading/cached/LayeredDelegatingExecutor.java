package apple.utilities.threading.cached;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

public class LayeredDelegatingExecutor implements Executor {


    private final List<DelegatingExecutorLayer> layers = new ArrayList<>(1);
    private final Executor fallback;

    public LayeredDelegatingExecutor(Executor fallback, Executor layer1) {
        this(fallback, layer1, 1);
    }

    public LayeredDelegatingExecutor(Executor fallback, Executor layer1, int maxCount) {
        this.fallback = fallback;
        if (maxCount <= 0) {
            throw new IllegalArgumentException("'maxCount' must be greater than 0. Provided: " + maxCount);
        }
        this.addLayer(new DelegatingExecutorLayer(layer1, maxCount));
    }

    public void addLayer(DelegatingExecutorLayer layer) {
        layers.add(layer);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        synchronized (layers) {
            for (DelegatingExecutorLayer layer : layers) {
                boolean success = layer.tryCountUp();
                if (!success) continue;

                CompletableFuture.runAsync(command, layer.getExecutor())
                    .thenRunAsync(layer::countDown);
                return;
            }
            fallback.execute(command);
        }
    }

}
