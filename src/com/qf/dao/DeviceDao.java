package com.qf.dao;

import com.qf.pojo.Car;
import com.qf.pojo.Device;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 13:42
 */
public interface DeviceDao {
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
    List<Device> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
