package com.qf.service.impl;

import com.qf.dao.DeviceDao;
import com.qf.dao.impl.DeviceDaoImpl;
import com.qf.pojo.Device;
import com.qf.pojo.Garden;
import com.qf.pojo.PageBean;
import com.qf.service.DeviceService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/30 9:32
 */
public class DeviceServiceImpl implements DeviceService {
    DeviceDao deviceDao = new DeviceDaoImpl();
    @Override
    public List<Device> selectAll() {
        return deviceDao.selectAll();
    }

    @Override
    public int addOne(Device device) {
        return deviceDao.addOne(device);
    }

    @Override
    public int deleteOne(int id) {
        return deviceDao.deleteOne(id);
    }

    @Override
    public Device selectOne(int id) {
        return deviceDao.selectOne(id);
    }

    @Override
    public int updateOne(Device device) {
        return deviceDao.updateOne(device);
    }

    @Override
    public PageBean<Device> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Device> deviceList = deviceDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = deviceDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Device> pageBean = new PageBean<>(deviceList,total,pages,cPage,pSize);
        return pageBean;
    }
}
