package com.qf.dao;

import com.qf.pojo.Device;
import com.qf.pojo.Free;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 13:42
 */
public interface FreeDao {
    //查询全部
    List<Free> selectAll();
    //添加一条数据
    int addOne(Free free);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Free selectOne(int id);
    //修改一条数据
    int updateOne(Free free);
    //分页查询
    List<Free> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
