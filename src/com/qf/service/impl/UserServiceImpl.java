package com.qf.service.impl;

import com.qf.dao.UserDao;
import com.qf.dao.impl.UserDaoImpl;
import com.qf.pojo.User;
import com.qf.service.UserService;

/**
 * 作者：SmallWood
 * 时间：2020/12/26 9:32
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();


    @Override
    public User selectOneByName(String username) {
        return userDao.selectOneByName(username);
    }

    @Override
    public User selectOneById(int id) {
        return null;
    }

    @Override
    public int updateOne(User user) {
        return userDao.updateOne(user);
    }
}
