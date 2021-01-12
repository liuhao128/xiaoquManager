package com.qf.service.impl;

import com.qf.dao.FreeDetailDao;
import com.qf.dao.impl.FreeDetailDaoImpl;
import com.qf.pojo.Device;
import com.qf.pojo.FreeDetail;
import com.qf.pojo.PageBean;
import com.qf.service.FreeDetailService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/30 11:05
 */
public class FreeDetailServiceImpl implements FreeDetailService {
    FreeDetailDao freeDetailDao = new FreeDetailDaoImpl();
    @Override
    public List<FreeDetail> selectAll() {
        return freeDetailDao.selectAll();
    }

    @Override
    public int addOne(FreeDetail freeDetail) {
        return freeDetailDao.addOne(freeDetail);
    }

    @Override
    public int deleteOne(int id) {
        return freeDetailDao.deleteOne(id);
    }

    @Override
    public FreeDetail selectOne(int id) {
        return freeDetailDao.selectOne(id);
    }

    @Override
    public int updateOne(FreeDetail freeDetail) {
        return freeDetailDao.updateOne(freeDetail);
    }

    @Override
    public PageBean<FreeDetail> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<FreeDetail> freeDetailList = freeDetailDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = freeDetailDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<FreeDetail> pageBean = new PageBean<>(freeDetailList,total,pages,cPage,pSize);
        return pageBean;
    }
}
