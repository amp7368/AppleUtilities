package apple.utilities.request.keyed.lazy;

import apple.utilities.request.settings.RequestSettingsBuilder;

@Deprecated
public class AppleRequestLazyServiceSimpleVoid extends AppleRequestLazyServiceSimple<Boolean> {
    public AppleRequestLazyServiceSimpleVoid(int requestsPerTimeUnit, int timeUnitMillis, int safeGuardBuffer, int lazinessMillis, int failSafeBuffer, RequestSettingsBuilder<Boolean> defaultSettings) {
        super(requestsPerTimeUnit, timeUnitMillis, safeGuardBuffer, lazinessMillis, failSafeBuffer, defaultSettings);
    }

    public AppleRequestLazyServiceSimpleVoid(int requestsPerTimeUnit, int timeUnitMillis, int safeGuardBuffer, int lazinessMillis, RequestSettingsBuilder<Boolean> defaultSettings) {
        super(requestsPerTimeUnit, timeUnitMillis, safeGuardBuffer, lazinessMillis, defaultSettings);
    }

    public AppleRequestLazyServiceSimpleVoid(int requestsPerTimeUnit, int timeUnitMillis, int safeGuardBuffer, int lazinessMillis, int failSafeBuffer) {
        super(requestsPerTimeUnit, timeUnitMillis, safeGuardBuffer, lazinessMillis, failSafeBuffer);
    }

    public AppleRequestLazyServiceSimpleVoid(int requestsPerTimeUnit, int timeUnitMillis, int safeGuardBuffer, int lazinessMillis) {
        super(requestsPerTimeUnit, timeUnitMillis, safeGuardBuffer, lazinessMillis);
    }
}
