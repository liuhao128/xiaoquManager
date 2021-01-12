package com.qf.service;

import com.qf.pojo.Device;
import com.qf.pojo.PageBean;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:11
 */
public interface DeviceService {
    //查询全部
    List<Device> selectAll();
    //添加一条数据
    int addOne(Device device);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Device selectOne(int id);
    //修改一条数据
    int updateOne(Device device);
    //分页查询
    PageBean<Device> selectByLimit(String keywords, String currentPage, String pageSize);
}
