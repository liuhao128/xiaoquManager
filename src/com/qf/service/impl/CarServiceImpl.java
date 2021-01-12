package com.qf.service.impl;

import com.qf.dao.CarDao;
import com.qf.dao.impl.CarDaoImpl;
import com.qf.pojo.Car;
import com.qf.pojo.PageBean;
import com.qf.pojo.Person;
import com.qf.service.CarService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 20:55
 */
public class CarServiceImpl implements CarService {
    CarDao carDao = new CarDaoImpl();
    @Override
    public List<Car> selectAll() {
        return carDao.selectAll();
    }

    @Override
    public int addOne(Car car) {
        return carDao.addOne(car);
    }

    @Override
    public int deleteOne(int id) {
        return carDao.deleteOne(id);
    }

    @Override
    public Car selectOne(int id) {
        return carDao.selectOne(id);
    }

    @Override
    public int updateOne(Car car) {
        return carDao.updateOne(car);
    }

    @Override
    public PageBean<Car> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Car> carList = carDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = carDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Car> pageBean = new PageBean<>(carList,total,pages,cPage,pSize);
        return pageBean;
    }
}
