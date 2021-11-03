package apple.utilities.request.keyed.lazy;

import apple.utilities.request.AppleRequest;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.keyed.AppleRequestKeyQueue;
import apple.utilities.request.keyed.AppleRequestOnConflict;
import apple.utilities.request.settings.RequestSettingsBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public abstract class AppleRequestLazyService<T> implements AppleRequestKeyQueue<T> {
    private final List<RequestHandlerTimed<T>> requests = new ArrayList<>();
    private final List<AppleRequestService.RequestCalled> pastRequests = new ArrayList<>();
    private final Map<Object, RequestHandlerTimed<T>> idToRequests = new HashMap<>();
    private boolean isRunningRequests = false;


    public abstract int getRequestsPerTimeUnit();

    public abstract int getTimeUnitMillis();

    public abstract int getSafeGuardBuffer();

    public abstract int getLazinessMillis();

    public int getFailSafeGuardBuffer() {
        return getSafeGuardBuffer() * 10;
    }

    public AppleRequestService.RequestHandler<T> queue(Object id,
                                                       AppleRequest<T> newRequest,
                                                       @Nullable Consumer<T> runAfter,
                                                       @Nullable RequestSettingsBuilder<T> builder,
                                                       AppleRequestOnConflict<T> requestConflict) {
        synchronized (requests) {
            RequestHandlerTimed<T> oldRequestHandler = idToRequests.get(id);
            if (oldRequestHandler == null) {
                if (runAfter == null) runAfter = (t) -> {
                };
                if (builder == null) builder = getDefaultSettings();
                oldRequestHandler = new RequestHandlerTimed<>(id, newRequest, runAfter, builder);
                requests.add(oldRequestHandler);
                this.idToRequests.put(id, oldRequestHandler);
            } else {
                AppleRequest<T> oldRequestGetter = oldRequestHandler.getRequest();
                oldRequestHandler.setRequest(requestConflict.onConflict(oldRequestGetter, newRequest));
                if (runAfter != null) oldRequestHandler.setRunAfter(runAfter);
                if (builder != null) oldRequestHandler.setBuilder(builder);
            }
            if (!isRunningRequests) {
                isRunningRequests = true;
                new Thread(this::eatRequests).start();
            }
            return oldRequestHandler;
        }
    }

    public RequestSettingsBuilder<T> getDefaultSettings() {
        return new RequestSettingsBuilder<>();
    }

    private void eatRequests() {
        RequestHandlerTimed<?> requestToRun;
        synchronized (requests) {
            if (requests.isEmpty()) {
                requestToRun = null;
                this.isRunningRequests = false;
            } else {
                requestToRun = requests.remove(0);
                idToRequests.remove(requestToRun.getId());
            }
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
            AppleRequestService.RequestCalled called = new AppleRequestService.RequestCalled(requestToRun, System.currentTimeMillis());
            synchronized (requests) {
                this.pastRequests.add(called);
                timeToNextRequest = failed ? Math.max(getFailSafeGuardBuffer(), checkTimeToNextRequestLazy()) : checkTimeToNextRequestLazy();
            }
            if (timeToNextRequest > 0) {
                try {
                    System.out.println(timeToNextRequest);
                    Thread.sleep(timeToNextRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (requests) {
                if (requests.isEmpty()) {
                    requestToRun = null;
                    this.isRunningRequests = false;
                } else {
                    requestToRun = requests.remove(0);
                    idToRequests.remove(requestToRun.getId());
                }
            }
        }
    }

    private long checkTimeToNextRequestLazy() {
        synchronized (requests) {
            long timeFromPast = checkTimeFromPastRequests();
            if (requests.isEmpty()) return timeFromPast;
            long timeToNextRequest = getLazinessMillis() - System.currentTimeMillis() - requests.get(0).getCalledTime();
            return Math.max(timeToNextRequest, timeFromPast);
        }
    }

    private long checkTimeFromPastRequests() {
        synchronized (requests) {
            long now = System.currentTimeMillis();
            Iterator<AppleRequestService.RequestCalled> iterator = pastRequests.iterator();
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

    public static class RequestHandlerTimed<T> extends AppleRequestService.RequestHandler<T> {
        private final long calledTimestamp = System.currentTimeMillis();
        private final Object id;

        public RequestHandlerTimed(Object id, AppleRequest<T> request, Consumer<T> runAfter, RequestSettingsBuilder<T> settings) {
            super(request, runAfter, settings);
            this.id = id;
        }

        public long getCalledTime() {
            return calledTimestamp;
        }

        public Object getId() {
            return id;
        }

        public synchronized void setRunAfter(@NotNull Consumer<T> runAfter) {
            this.runAfter = runAfter;
        }

        public synchronized void setBuilder(@NotNull RequestSettingsBuilder<T> settings) {
            this.exceptionHandler = settings.getExceptionHandler();
            this.logger = settings.getLogger();
        }

        public AppleRequest<T> getRequest() {
            return request;
        }

        public synchronized void setRequest(@NotNull AppleRequest<T> newRequest) {
            this.request = newRequest;
        }
    }
}
