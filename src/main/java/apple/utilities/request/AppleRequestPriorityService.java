package apple.utilities.request;

import apple.utilities.request.settings.RequestPrioritySettingsBuilder;
import apple.utilities.request.settings.RequestPrioritySettingsBuilderVoid;
import apple.utilities.request.settings.RequestSettingsBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public abstract class AppleRequestPriorityService<Priority extends AppleRequestPriorityService.AppleRequestPriority>
        implements AppleRequestQueue {
    private final Map<Priority, List<AppleRequestService.RequestHandler<?>>> requests = new HashMap<>();
    private final List<AppleRequestService.RequestCalled> pastRequests = new ArrayList<>();
    private boolean isRunningRequests = false;

    public AppleRequestPriorityService() {
        for (Priority priority : getPriorities()) {
            requests.put(priority, new ArrayList<>());
        }
    }

    public AppleRequestService.RequestHandler<?> queueVoid(AppleRequestVoid request, RequestPrioritySettingsBuilderVoid<Priority> settings) {
        return queueVoid(request, () -> {
        }, settings);
    }


    public AppleRequestService.RequestHandler<?> queueVoid(AppleRequestVoid request, Runnable runAfter, RequestPrioritySettingsBuilderVoid<Priority> settings) {
        return queue(request, (e) -> runAfter.run(), settings);
    }

    public <T> AppleRequestService.RequestHandler<T> queuePriority(AppleRequest<T> request, Consumer<T> runAfter, RequestPrioritySettingsBuilder<T, Priority> settingsBuilder) {
        synchronized (this) {
            AppleRequestService.RequestHandler<T> requestHandler = new AppleRequestService.RequestHandler<>(request, runAfter, settingsBuilder);
            @Nullable Priority priority = settingsBuilder.getPriority();
            if (priority == null) priority = getDefaultPriority();
            requests.get(priority).add(requestHandler);
            if (!isRunningRequests) {
                isRunningRequests = true;
                new Thread(this::eatRequests).start();
            }
            return requestHandler;
        }
    }

    @Override
    public <T> AppleRequestService.RequestHandler<T> queue(AppleRequest<T> request, Consumer<T> runAfter, RequestSettingsBuilder<T> settingsBuilder) {
        return queuePriority(request, runAfter, new RequestPrioritySettingsBuilder<T, Priority>(settingsBuilder).withPriority(getDefaultPriority()));
    }

    public <T> RequestSettingsBuilder<T> getDefaultSettings() {
        return getDefaultPrioritySettings();
    }

    public <T> RequestPrioritySettingsBuilder<T, Priority> getDefaultPrioritySettings() {
        return new RequestPrioritySettingsBuilder<T, Priority>().withPriority(getDefaultPriority());
    }

    private void eatRequests() {
        AppleRequestService.RequestHandler<?> requestToRun;
        @Nullable Priority nowPriority;
        synchronized (this) {
            nowPriority = getNowPriority();
            if (nowPriority == null) {
                this.isRunningRequests = false;
                return;
            }
            requestToRun = requests.get(nowPriority).remove(0);
        }
        while (requestToRun != null) {
            boolean failed = false;
            try {
                requestToRun.doRequest();
                AppleRequestService.RequestCalled called = new AppleRequestService.RequestCalled(requestToRun, System.currentTimeMillis());
                synchronized (this) {
                    this.pastRequests.add(called);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                failed = true;
                synchronized (this) {
                    requests.get(nowPriority).add(requestToRun);
                }
            }
            long timeToNextRequest;
            synchronized (this) {
                timeToNextRequest = checkTimeToNextRequest(failed);
            }
            if (timeToNextRequest > 0) {
                try {
                    Thread.sleep(timeToNextRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (this) {
                nowPriority = getNowPriority();
                if (nowPriority == null) {
                    this.isRunningRequests = false;
                    return;
                }
                requestToRun = requests.get(nowPriority).remove(0);
            }
        }

    }

    @Nullable
    private Priority getNowPriority() {
        synchronized (this) {
            for (Priority priority : getPriorities()) {
                if (!requests.get(priority).isEmpty()) return priority;
            }
        }
        return null;
    }

    private long checkTimeToNextRequest(boolean failed) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            Iterator<AppleRequestService.RequestCalled> iterator = pastRequests.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isOld(getTimeUnitMillis(), now))
                    iterator.remove();
                else
                    break;
            }
            Priority priorityNow = getNowPriority();
            if (priorityNow == null) {
                // this part really shouldn't happen
                return 0;
            }
            if (pastRequests.size() < priorityNow.getRequestsPerTimeUnit()) {
                return priorityNow.getBuffer(failed);
            } else {
                final long timeToWait = pastRequests.isEmpty() ? 0 : pastRequests.get(0).timeUntilOld(getTimeUnitMillis(), now);
                return Math.max(priorityNow.getBuffer(failed), timeToWait);
            }
        }
    }


    protected abstract Priority[] getPriorities();

    protected abstract Priority getDefaultPriority();

    protected abstract int getTimeUnitMillis();

    public interface AppleRequestPriority {
        int getRequestsPerTimeUnit();

        int getSafeGuardBuffer();

        default int getFailGuardBuffer() {
            return getSafeGuardBuffer() * 10;
        }

        default int getBuffer(boolean failed) {
            return failed ? getFailGuardBuffer() : getSafeGuardBuffer();
        }
    }
}
