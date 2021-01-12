package com.qf.service.impl;

import com.qf.dao.PetDao;
import com.qf.dao.impl.PetDaoImpl;
import com.qf.pojo.Car;
import com.qf.pojo.PageBean;
import com.qf.pojo.Pet;
import com.qf.service.PetService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/30 0:21
 */
public class PetServiceImpl implements PetService {
    PetDao petDao = new PetDaoImpl();
    @Override
    public List<Pet> selectAll() {
        return petDao.selectAll();
    }

    @Override
    public int addOne(Pet pet) {
        return petDao.addOne(pet);
    }

    @Override
    public int deleteOne(int id) {
        return petDao.deleteOne(id);
    }

    @Override
    public Pet selectOne(int id) {
        return petDao.selectOne(id);
    }

    @Override
    public int updateOne(Pet pet) {
        return petDao.updateOne(pet);
    }

    @Override
    public PageBean<Pet> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Pet> petList = petDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = petDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Pet> pageBean = new PageBean<>(petList,total,pages,cPage,pSize);
        return pageBean;
    }
}
