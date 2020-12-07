package com.itszt.job;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

/**
 * 简单定时任务执行
 */
public class Testjob {


    public static void main(String[] args) throws SchedulerException {
        //通过 SchedulerFactory 来获取一个调度器
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        Scheduler scheduler = schedulerFactory.getScheduler();

        //引进作业程序
        JobDetail jobDetail = new JobDetailImpl("lijinjian", "xiaoxiangzu", QuartzTask.class);
        //new 一个触发器
        SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl("lijinjian", "xiaoxiangzu");

        long startTime = System.currentTimeMillis();
        simpleTrigger.setStartTime(new Date(startTime));

        //设置作业执行间隔
        simpleTrigger.setRepeatInterval(1000);

        //设置作业执行次数
        simpleTrigger.setRepeatCount(100);

        //设置作业执行优先级默认为5
        simpleTrigger.setPriority(5);

        //作业和触发器设置到调度器中
        scheduler.scheduleJob(jobDetail, simpleTrigger);

        //启动调度器
        scheduler.start();
    }
}
