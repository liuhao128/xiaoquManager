package com.qf.service;

import com.qf.pojo.Building;
import com.qf.pojo.Garden;
import com.qf.pojo.House;
import com.qf.pojo.PageBean;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/28 16:21
 */
public interface HouseService {
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
    PageBean<House> selectByLimit(String keywords,String currentPage, String pageSize);
}
