package apple.utilities.database.concurrent.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class AJDFileOpAsyncBase<A, R> {

    private final Executor executor;
    private final Function<A, R> asyncOp;
    // The Future to return next
    private CompletableFuture<R> currentOp;
    private CompletableFuture<Void> runningTask;
    private CompletableFuture<R> queuedOp;
    private A queuedArg;
    private boolean waitToStart = false;

    public AJDFileOpAsyncBase(Function<A, R> asyncOp, Executor executor) {
        this.asyncOp = asyncOp;
        this.executor = executor;
    }

    /**
     * set this.runningTask to the FutureTask
     *
     * @param arg The input to use for asyncOp
     * @implNote Should only be called while synchronized
     * @implNote field this.currentOp should be set to the future that #runTask is meant to complete after running
     * @see #runTask(A arg)
     */
    private void runTask(A arg) {
        CompletableFuture<R> currentOpLocal = this.currentOp;
        Runnable completeCurrentOp = () -> {
            currentOpLocal.complete(this.asyncOp.apply(arg));
            this.finishOp();
        };
        this.runningTask = CompletableFuture.runAsync(completeCurrentOp, this.executor);
    }

    public CompletableFuture<R> tryStart(A arg) {
        synchronized (this) {
            waitToStart();

            // if nothing is running, start running
            if (this.runningTask == null || this.runningTask.isDone()) {
                this.currentOp = new CompletableFuture<>();
                runTask(arg);
                return this.currentOp;
            }
            // queue the start
            this.queuedArg = arg;
            if (this.queuedOp == null)
                this.queuedOp = new CompletableFuture<>();
            return this.queuedOp;
        }
    }

    public R completeNow(A arg) {
        CompletableFuture<R> opToWait;
        CompletableFuture<R> opToReturn;
        synchronized (this) {
            waitToStart();
            opToWait = this.currentOp;
            opToReturn = this.queuedOp;
            this.waitToStart = true;
            this.queuedOp = null;
            this.queuedArg = null;
        }
        try {
            if (opToWait != null) opToWait.get();
            R returnVal = this.asyncOp.apply(arg);
            if (opToReturn != null) opToReturn.complete(returnVal);
            return returnVal;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            synchronized (this) {
                this.waitToStart = false;
                this.notifyAll();
            }
        }
    }

    private void waitToStart() {
        while (this.waitToStart) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void finishOp() {
        synchronized (this) {
            if (this.queuedOp != null) {
                A arg = queuedArg;
                this.currentOp = this.queuedOp;
                this.queuedArg = null;
                this.queuedOp = null;
                runTask(arg);
            } else {
                this.currentOp = null;
                this.runningTask = null;
            }
        }
    }
}
