package apple.utilities.threading.service.base.failure;

public class TaskFailureNoRequeue extends TaskFailureProcedure {
    @Override
    public boolean shouldRetryNow() {
        return false;
    }

    @Override
    public void finishFailure(Runnable queueToHandler) {
    }
}
