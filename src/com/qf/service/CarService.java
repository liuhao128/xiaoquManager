package com.qf.service;

import com.qf.pojo.Car;
import com.qf.pojo.PageBean;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:11
 */
public interface CarService {
    //查询全部
    List<Car> selectAll();
    //添加一条数据
    int addOne(Car car);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Car selectOne(int id);
    //修改一条数据
    int updateOne(Car car);
    //分页查询
    PageBean<Car> selectByLimit(String keywords, String currentPage, String pageSize);
}
