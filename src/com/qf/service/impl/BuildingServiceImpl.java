package com.qf.service.impl;

import com.qf.dao.BuildingDao;
import com.qf.dao.impl.BuildingDaoImpl;
import com.qf.pojo.Building;
import com.qf.pojo.House;
import com.qf.pojo.PageBean;
import com.qf.service.BuildingService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:16
 */
public class BuildingServiceImpl implements BuildingService {
    BuildingDao buildingDao = new BuildingDaoImpl();
    @Override
    public List<Building> selectAll() {
        return buildingDao.selectAll();
    }

    @Override
    public int addOne(Building building) {
        return buildingDao.addOne(building);
    }

    @Override
    public int deleteOne(int id) {
        return buildingDao.deleteOne(id);
    }

    @Override
    public Building selectOne(int id) {
        return buildingDao.selectOne(id);
    }

    @Override
    public int updateOne(Building building) {
        return buildingDao.updateOne(building);
    }

    @Override
    public PageBean<Building> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Building> buildingList = buildingDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = buildingDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Building> pageBean = new PageBean<>(buildingList,total,pages,cPage,pSize);
        return pageBean;
    }
}
