package com.itszt.job;

import com.itszt.domain.OrderJob;
import com.itszt.service.JobService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Quartz定时任务
 */
@Data
@NoArgsConstructor
public class QuartzTask implements Job {

    ReentrantLock reentrantLock = new ReentrantLock(true);

    @Autowired
    private JobService jobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        OrderJob job = new OrderJob();
        job.setCreateTime(LocalDateTime.now());
        job.setStartDate(LocalDateTime.now());
        job.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        System.out.println("job = " + job);
//        jobService.saveOne(job);
        System.out.println("定时任务执行中:" + Thread.currentThread().getName());
    }


}
