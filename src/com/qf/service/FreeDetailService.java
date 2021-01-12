package com.qf.service;

import com.qf.pojo.FreeDetail;
import com.qf.pojo.PageBean;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:12
 */
public interface FreeDetailService {
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
    PageBean<FreeDetail> selectByLimit(String keywords, String currentPage, String pageSize);
}
