package apple.utilities.threading;

import apple.utilities.threading.util.ThreadFactory;

public interface Daemon extends ThreadFactory {
    default void init() {
        this.startThread(this::init1);
    }

    private void init1() {
        onStart();
        if (isSleepFirst()) doSleep();
        while (true) {
            if (shouldStop()) break;
            try {
                run();
            } catch (Exception e) {
                onException(e);
            }
            if (doSleep()) break;
        }
        onComplete();
    }

    private boolean doSleep() {
        try {
            Thread.sleep(getSleepMillis());
        } catch (InterruptedException e) {
            onException(e);
            // if we can't sleep, stop running
            return true;
        }
        return false;
    }


    void run();

    long getSleepMillis();

    default void onException(Exception e) {
        e.printStackTrace();
    }

    default void onComplete() {
    }

    default void onStart() {
    }

    default boolean shouldStop() {
        return false;
    }

    default boolean isSleepFirst() {
        return false;
    }
}
