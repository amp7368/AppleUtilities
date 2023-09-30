package apple.utilities.threading.service.base.handler;

public class RateLimit {

    private long nextAllowedRun = 0;
    private int failCount = 0;
    private long clearFailsLater = 1000 * 60;
    private long originalDelayOnFail = 1000;
    private long currentDelayOnFail = originalDelayOnFail;

    public void incrementFail() {
        this.checkClearFails();
        this.failCount++;
        this.nextAllowedRun = System.currentTimeMillis() + currentDelayOnFail;
        this.currentDelayOnFail *= 2;
    }

    private void checkClearFails() {
        if (this.isFailing() && this.nextAllowedRun + this.clearFailsLater < System.currentTimeMillis()) {
            this.failCount = 0;
            this.currentDelayOnFail = originalDelayOnFail;
        }
    }

    private boolean isFailing() {
        return this.failCount != 0;
    }

    public long getDelay() {
        this.checkClearFails();
        return this.isFailing() ? System.currentTimeMillis() - this.nextAllowedRun : 0;
    }
}
