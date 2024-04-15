package apple.utilities.database.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrentAJDConfig {

    private static Executor sharedAJDInst = Executors.newCachedThreadPool();
    /**
     * This is Executors#cachedThreadPool() except corePoolSize is 1 and keepAlive is over 1 min.
     * Keep at least 1 thread alive for saving and loading files to remove thread creation costs of
     * very inactive programs. This does use slightly more memory of that one thread, but there's only 1
     * extra for the entire program
     */
    private static Executor l1Cache = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
        90L, TimeUnit.SECONDS, new SynchronousQueue<>());

    public static Executor sharedAJDInst() {
        return sharedAJDInst;
    }

    public static void sharedAJDInst(Executor sharedAJDInst) {
        ConcurrentAJDConfig.sharedAJDInst = sharedAJDInst;
    }

    public static Executor l1Cache() {
        return l1Cache;
    }

    public static void l1Cache(Executor l1Cache) {
        ConcurrentAJDConfig.l1Cache = l1Cache;
    }
}
