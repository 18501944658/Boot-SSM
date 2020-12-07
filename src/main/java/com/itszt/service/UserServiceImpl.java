package com.itszt.service;

import com.itszt.common.BaseParmerters;
import com.itszt.domain.User;
import com.itszt.repositry.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Override
    public List<User> selecctAll(BaseParmerters baseParmerters) {
        return userDao.selectAll();
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertOne(User user) {
        return userDao.insert(user);
    }

    @Override
    public void insertMany(List<User> users) {
        userDao.insertMany(users);
    }
}
