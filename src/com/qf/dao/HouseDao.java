package com.qf.dao;

import com.qf.pojo.Garden;
import com.qf.pojo.House;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/28 15:48
 */
public interface HouseDao {
    //查询全部
    List<House> selectAll();
    //添加一条数据
    int addOne(House house);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    House selectOne(int id);
    //修改一条数据
    int updateOne(House house);
    //分页查询
    List<House> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
