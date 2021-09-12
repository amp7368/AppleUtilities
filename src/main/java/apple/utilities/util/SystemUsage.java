package apple.utilities.util;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class SystemUsage {
    private static final OperatingSystemMXBean OS;
    private static final long UPDATE_INTERVAL = 2000;
    private static long lastCheckedProcessorsFree = 0;
    private static double processorsFree = 0;

    static {
        java.lang.management.OperatingSystemMXBean s = ManagementFactory.getOperatingSystemMXBean();
        if (s instanceof com.sun.management.OperatingSystemMXBean os) {
            OS = os;
        } else {
            OS = null;
        }
    }

    /**
     * @return a value between 0 and 1 depending on cpu usage
     */
    public static double getProcessCpuLoad() {
        return OS.getCpuLoad();
    }

    /**
     * @return the available processors available to the os
     */
    public static double getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static double getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static double getTotalMemory() {
        return Runtime.getRuntime().maxMemory();
    }


    /**
     * @return (1 - getProcessCpuLoad ()) * getProcessorCount()
     */
    public synchronized static double getProcessorsFree() {
        if (System.currentTimeMillis() - lastCheckedProcessorsFree > UPDATE_INTERVAL) {
            processorsFree = getProcessorCount() * (1 - getProcessCpuLoad());
            lastCheckedProcessorsFree = System.currentTimeMillis();
        }
        return processorsFree;
    }
}
