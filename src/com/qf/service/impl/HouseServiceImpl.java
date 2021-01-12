package com.qf.service.impl;

import com.qf.dao.HouseDao;
import com.qf.dao.impl.HouseDaoImpl;
import com.qf.pojo.Garden;
import com.qf.pojo.House;
import com.qf.pojo.PageBean;
import com.qf.service.HouseService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/28 16:27
 */
public class HouseServiceImpl implements HouseService {
    HouseDao houseDao = new HouseDaoImpl();

    @Override
    public List<House> selectAll() {
        return houseDao.selectAll();
    }

    @Override
    public int addOne(House house) {
        return houseDao.addOne(house);
    }

    @Override
    public int deleteOne(int id) {
        return houseDao.deleteOne(id);
    }

    @Override
    public House selectOne(int id) {
        return houseDao.selectOne(id);
    }

    @Override
    public int updateOne(House house) {
        return houseDao.updateOne(house);
    }

    @Override
    public PageBean<House> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<House> houseList = houseDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = houseDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<House> pageBean = new PageBean<>(houseList,total,pages,cPage,pSize);
        return pageBean;
    }
}
