package com.qf.service.impl;

import com.qf.dao.FreeDao;
import com.qf.dao.impl.FreeDaoImpl;
import com.qf.pojo.Device;
import com.qf.pojo.Free;
import com.qf.pojo.PageBean;
import com.qf.service.FreeService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/30 10:16
 */
public class FreeServiceImpl implements FreeService {
    FreeDao freeDao = new FreeDaoImpl();
    @Override
    public List<Free> selectAll() {
        return freeDao.selectAll();
    }

    @Override
    public int addOne(Free free) {
        return freeDao.addOne(free);
    }

    @Override
    public int deleteOne(int id) {
        return freeDao.deleteOne(id);
    }

    @Override
    public Free selectOne(int id) {
        return freeDao.selectOne(id);
    }

    @Override
    public int updateOne(Free free) {
        return freeDao.updateOne(free);
    }

    @Override
    public PageBean<Free> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Free> freeList = freeDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = freeDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Free> pageBean = new PageBean<>(freeList,total,pages,cPage,pSize);
        return pageBean;
    }
}
