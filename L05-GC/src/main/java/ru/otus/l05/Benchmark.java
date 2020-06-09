package ru.otus.l05;

import static java.lang.String.format;

import com.sun.management.GarbageCollectionNotificationInfo;
import lombok.Data;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Benchmark.
 *
 * @author Evgeniya_Yanchenko
 */
public class Benchmark implements BenchmarkMBean {

    private List<Integer> list = new ArrayList<>();
    private Map<String, GCStatistics> gcResults = new HashMap<>();


    void run() throws InterruptedException {
        Random random = new Random();
        while (true) {
            for (int i = 0; i < 100; i++) {
                list.add(random.nextInt());
            }
            list.remove(list.size() - 1);
            Thread.sleep(1);
        }
    }

    @Override
    public void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcBeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("GC name: " + gcBean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    long duration = info.getGcInfo().getDuration();

                    if (gcResults.get(gcName) == null) {
                        gcResults.put(gcName, new GCStatistics());
                    }

                    GCStatistics gcStatistics = gcResults.get(gcName);
                    gcStatistics.setRuns(gcStatistics.getRuns() + 1);
                    gcStatistics.setDuration(gcStatistics.getDuration() + duration);

                    System.out.println("Name: " + gcName + ", action: " + gcAction + ", duration: " + duration + " ms");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    @Override
    public void printResult() {
        gcResults.forEach(
                (gcName, gcData) -> System.out.println(format("%s: количество запусков - %d, общее время работы - %d мс",
                        gcName, gcData.getRuns(), gcData.getDuration()))
        );
    }


    @Data
    private static class GCStatistics {
        private int runs;
        private long duration;
    }

}
