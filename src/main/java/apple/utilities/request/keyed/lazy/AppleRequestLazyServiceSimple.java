package apple.utilities.request.keyed.lazy;

import apple.utilities.request.settings.RequestSettingsBuilder;

@Deprecated
public class AppleRequestLazyServiceSimple<T> extends AppleRequestLazyService<T> {
    private final int requestsPerTimeUnit;
    private final int timeUnitMillis;
    private final int safeGuardBuffer;
    private final int lazinessMillis;
    private final boolean failSafeBufferSet;
    private final int failSafeBuffer;
    private final boolean defaultSettingsSet;
    private final RequestSettingsBuilder<T> defaultSettings;

    public AppleRequestLazyServiceSimple(int requestsPerTimeUnit,
                                         int timeUnitMillis,
                                         int safeGuardBuffer,
                                         int lazinessMillis,
                                         int failSafeBuffer,
                                         RequestSettingsBuilder<T> defaultSettings) {
        this.requestsPerTimeUnit = requestsPerTimeUnit;
        this.timeUnitMillis = timeUnitMillis;
        this.safeGuardBuffer = safeGuardBuffer;
        this.lazinessMillis = lazinessMillis;
        this.failSafeBufferSet = true;
        this.failSafeBuffer = failSafeBuffer;
        this.defaultSettingsSet = true;
        this.defaultSettings = defaultSettings;
    }

    public AppleRequestLazyServiceSimple(int requestsPerTimeUnit,
                                         int timeUnitMillis,
                                         int safeGuardBuffer,
                                         int lazinessMillis,
                                         RequestSettingsBuilder<T> defaultSettings) {
        this.requestsPerTimeUnit = requestsPerTimeUnit;
        this.timeUnitMillis = timeUnitMillis;
        this.safeGuardBuffer = safeGuardBuffer;
        this.lazinessMillis = lazinessMillis;
        this.failSafeBufferSet = false;
        this.failSafeBuffer = 0;
        this.defaultSettingsSet = true;
        this.defaultSettings = defaultSettings;
    }

    public AppleRequestLazyServiceSimple(int requestsPerTimeUnit,
                                         int timeUnitMillis,
                                         int safeGuardBuffer,
                                         int lazinessMillis,
                                         int failSafeBuffer) {
        this.requestsPerTimeUnit = requestsPerTimeUnit;
        this.timeUnitMillis = timeUnitMillis;
        this.safeGuardBuffer = safeGuardBuffer;
        this.lazinessMillis = lazinessMillis;
        this.failSafeBufferSet = true;
        this.failSafeBuffer = failSafeBuffer;
        this.defaultSettingsSet = false;
        this.defaultSettings = null;
    }

    public AppleRequestLazyServiceSimple(int requestsPerTimeUnit,
                                         int timeUnitMillis,
                                         int safeGuardBuffer,
                                         int lazinessMillis) {
        this.requestsPerTimeUnit = requestsPerTimeUnit;
        this.timeUnitMillis = timeUnitMillis;
        this.safeGuardBuffer = safeGuardBuffer;
        this.lazinessMillis = lazinessMillis;
        this.failSafeBufferSet = false;
        this.failSafeBuffer = 0;
        this.defaultSettingsSet = false;
        this.defaultSettings = null;
    }


    @Override
    public int getRequestsPerTimeUnit() {
        return this.requestsPerTimeUnit;
    }

    @Override
    public int getTimeUnitMillis() {
        return timeUnitMillis;
    }

    @Override
    public int getSafeGuardBuffer() {
        return safeGuardBuffer;
    }

    @Override
    public int getLazinessMillis() {
        return lazinessMillis;
    }

    @Override
    public int getFailSafeGuardBuffer() {
        return this.failSafeBufferSet ? this.failSafeBuffer : super.getFailSafeGuardBuffer();
    }

    @Override
    public RequestSettingsBuilder<T> getDefaultSettings() {
        return this.defaultSettingsSet ? this.defaultSettings : super.getDefaultSettings();
    }
}
