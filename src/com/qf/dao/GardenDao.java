package com.qf.dao;

import com.qf.pojo.Garden;
import com.qf.pojo.PageBean;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:49
 */
public interface GardenDao {
    //查询全部
    List<Garden> selectAll();
    //添加一条数据
    int addOne(Garden garden);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Garden selectOne(int id);
    //修改一条数据
    int updateOne(Garden garden);
    //分页查询
    List<Garden> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count(String keyword);
    //模糊查询
    List<Garden> selectLike(String name);

}
