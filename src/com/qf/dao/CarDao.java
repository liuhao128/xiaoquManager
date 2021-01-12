package com.qf.dao;

import com.qf.pojo.Building;
import com.qf.pojo.Car;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 13:40
 */
public interface CarDao {
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
    List<Car> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
