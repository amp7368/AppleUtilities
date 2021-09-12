package apple.utilities.event;

public class AppleEvent {
    private boolean isCanceled = false;

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        this.isCanceled = canceled;
    }
}
