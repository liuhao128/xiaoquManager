package com.qf.dao;

import com.qf.pojo.Building;
import com.qf.pojo.House;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 13:39
 */
public interface BuildingDao {
    //查询全部
    List<Building> selectAll();
    //添加一条数据
    int addOne(Building building);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Building selectOne(int id);
    //修改一条数据
    int updateOne(Building building);
    //分页查询
    List<Building> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
