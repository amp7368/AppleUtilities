package apple.utilities.lamdas.daemon;

public interface AppleDaemon {
    default void start() {
        onPreInit();
        new Thread(this::daemon_).start();
        onPostInit();
    }

    private void daemon_() {
        while (true) {
            try {
                this.daemon();
                sleep();
            } catch (Exception e) {
                error(e);
            }
        }
    }

    default void sleep() {
        try {
            Thread.sleep(getSleepTime());
        } catch (Exception e) {
            error(e);
        }
    }

    default void error(Exception e) {
        e.printStackTrace();
    }

    default void onPreInit() {
    }

    default void onPostInit() {
    }

    long getSleepTime();

    void daemon();
}
