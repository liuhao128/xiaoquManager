package com.qf.service;

import com.qf.pojo.User;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 19:44
 */
public interface UserService {

    //根据username查询一条数据
    User selectOneByName(String username);
    //根据id查询一条数据
    User selectOneById(int id);
    //修改一条数据
    int updateOne(User user);
}
