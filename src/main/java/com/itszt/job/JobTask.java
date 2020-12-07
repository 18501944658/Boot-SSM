package com.itszt.job;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobTask {
    /**
     * 定时线程池,定时推迟多久执行
     *
     * @param args
     */
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "run");
                }
            }, 10, TimeUnit.SECONDS);
        }
    }
}
