package com.qf.service;

import com.qf.pojo.PageBean;
import com.qf.pojo.Person;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:12
 */
public interface PersonService {
    //查询全部
    List<Person> selectAll();
    //添加一条数据
    int addOne(Person person);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Person selectOne(int id);
    //修改一条数据
    int updateOne(Person person);
    //分页查询
    PageBean<Person> selectByLimit(String keywords, String currentPage, String pageSize);
}
