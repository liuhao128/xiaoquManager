package com.qf.service;

import com.qf.pojo.Free;
import com.qf.pojo.PageBean;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:12
 */
public interface FreeService {
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
    PageBean<Free> selectByLimit(String keywords, String currentPage, String pageSize);
}
