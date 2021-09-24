package apple.utilities.request;

import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.structures.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class AppleRequestService implements AppleRequestQueue {
    private final List<RequestHandler<?>> requests = new ArrayList<>();
    private final List<RequestCalled> pastRequests = new ArrayList<>();
    private boolean isRunningRequests = false;

    public abstract int getRequestsPerTimeUnit();

    public abstract int getTimeUnitMillis();

    public abstract int getSafeGuardBuffer();

    public int getFailSafeGuardBuffer() {
        return getSafeGuardBuffer() * 10;
    }

    @Override
    public <T> RequestHandler<T> queue(AppleRequest<T> request, Consumer<T> runAfter, RequestSettingsBuilder<T> builder) {
        synchronized (this) {
            RequestHandler<T> requestHandler = new RequestHandler<>(request, runAfter, builder);
            requests.add(requestHandler);
            if (!isRunningRequests) {
                isRunningRequests = true;
                new Thread(this::eatRequests).start();
            }
            return requestHandler;
        }
    }

    @Override
    public <T> RequestSettingsBuilder<T> getDefaultSettings() {
        return new RequestSettingsBuilder<>();
    }

    private void eatRequests() {
        RequestHandler<?> requestToRun;
        synchronized (this) {
            if (requests.isEmpty()) {
                requestToRun = null;
                this.isRunningRequests = false;
            } else requestToRun = requests.remove(0);
        }
        while (requestToRun != null) {
            boolean failed = false;
            try {
                requestToRun.doRequest();
            } catch (Exception exception) {
                exception.printStackTrace();
                failed = true;
            }
            long timeToNextRequest;
            RequestCalled called = new RequestCalled(requestToRun, System.currentTimeMillis());
            synchronized (this) {
                this.pastRequests.add(called);
                timeToNextRequest = failed ? Math.max(getFailSafeGuardBuffer(), checkTimeToNextRequest()) : checkTimeToNextRequest();
            }
            if (timeToNextRequest > 0) {
                try {
                    Thread.sleep(timeToNextRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (this) {
                requestToRun = requests.isEmpty() ? null : requests.remove(0);
                if (requestToRun == null) {
                    this.isRunningRequests = false;
                }
            }
        }
    }

    private long checkTimeToNextRequest() {
        synchronized (this) {
            long now = System.currentTimeMillis();
            Iterator<RequestCalled> iterator = pastRequests.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isOld(getTimeUnitMillis(), now))
                    iterator.remove();
                else
                    break;
            }
            if (pastRequests.size() < getRequestsPerTimeUnit()) {
                return 0;
            } else {
                if (pastRequests.isEmpty()) return 0;
                return pastRequests.get(0).timeUntilOld(getTimeUnitMillis(), now);
            }
        }
    }


    public static class RequestHandler<T> {
        protected AppleRequest<T> request;
        protected Consumer<T> runAfter;
        protected List<Pair<ExceptionHandler, Integer>> exceptionHandler;
        protected RequestLogger<T> logger;

        private boolean isWaiting = false;
        private T gotten = null;
        private boolean done = false;
        private boolean ranAfter = false;

        public RequestHandler(AppleRequest<T> request, Consumer<T> runAfter, RequestSettingsBuilder<T> settings) {
            this.request = request;
            this.runAfter = runAfter;
            this.exceptionHandler = settings.getExceptionHandler();
            this.logger = settings.getLogger();
        }

        public synchronized void doRequest() {
            logger.startRequest();
            try {
                gotten = request.get();
            } catch (Exception e) {
                if (!tryCatchException(e)) ExceptionHandler.throwE(e);
            }
            try {
                finish();
            } catch (Exception e) {
                tryCatchException(e);
            }
        }


        private synchronized boolean tryCatchException(Exception e) {
            logger.exceptionHandle(e);
            for (Pair<ExceptionHandler, Integer> exceptionHandlerIntegerPair : exceptionHandler) {
                try {
                    ExceptionHandler handler = exceptionHandlerIntegerPair.getKey();
                    handler.accept(e);
                    return true;
                } catch (Exception ignored) {
                }
            }
            logger.exceptionUncaught(e);
            return false;
        }

        private synchronized void finish() {
            logger.startDone(gotten);
            synchronized (this) {
                done = true;
                if (!isWaiting) {
                    runAfter.accept(gotten);
                    ranAfter = true;
                }
                this.notify();
            }
            logger.finishDone(gotten);
        }

        public T complete() {
            synchronized (this) {
                if (done) return gotten;
                isWaiting = true;
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return gotten;
            }
        }

        public T completeAndRun() {
            gotten = complete();
            if (!ranAfter) runAfter.accept(gotten);
            ranAfter = true;
            return gotten;
        }
    }

    public static final class RequestCalled {
        private final RequestHandler<?> request;
        private final long timeRequested;

        public RequestCalled(RequestHandler<?> request, long timeRequested) {
            this.request = request;
            this.timeRequested = timeRequested;
        }

        public boolean isOld(int oldThreshold, long now) {
            return now > oldThreshold + timeRequested;
        }

        public long timeUntilOld(long oldThreshold, long now) {
            return oldThreshold + timeRequested - now;
        }

        public RequestHandler<?> request() {
            return request;
        }

        public long timeRequested() {
            return timeRequested;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            RequestCalled that = (RequestCalled) obj;
            return Objects.equals(this.request, that.request) &&
                    this.timeRequested == that.timeRequested;
        }

        @Override
        public int hashCode() {
            return Objects.hash(request, timeRequested);
        }

        @Override
        public String toString() {
            return "RequestCalled[" +
                    "request=" + request + ", " +
                    "timeRequested=" + timeRequested + ']';
        }

    }
}
