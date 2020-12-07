package com.itszt.service;

import com.itszt.domain.OrderJob;
import com.itszt.repositry.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    @Override
    public void saveOne(OrderJob job) {
        jobDao.insert(job);
    }
}
