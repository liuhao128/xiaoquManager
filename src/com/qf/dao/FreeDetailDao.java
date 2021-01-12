package com.qf.dao;

import com.qf.pojo.Free;
import com.qf.pojo.FreeDetail;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 13:42
 */
public interface FreeDetailDao {
    //查询全部
    List<FreeDetail> selectAll();
    //添加一条数据
    int addOne(FreeDetail freeDetail);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    FreeDetail selectOne(int id);
    //修改一条数据
    int updateOne(FreeDetail freeDetail);
    //分页查询
    List<FreeDetail> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
