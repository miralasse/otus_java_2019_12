package ru.otus.l05;

import static java.lang.String.format;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * GCTest.
 *
 * @author Evgeniya_Yanchenko
 */

/*
-Xms128m
-Xmx128m
-XX:+HeapDumpOnOutOfMemoryError
-XX:+UseSerialGC
-XX:+UseParallelGC
-XX:+UseG1GC
 */

public class GCTest {
    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        long beginTime = System.currentTimeMillis();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);

        try {
            mbean.switchOnMonitoring();
            mbean.run();
        } catch (OutOfMemoryError e) {
            System.out.println("Out Of Memory Error");
            e.printStackTrace();
        } finally {
            System.out.println(format("Общее время работы: %d сек", (System.currentTimeMillis() - beginTime) / 1000));
            mbean.printResult();
        }
    }
}
