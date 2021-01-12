package com.qf.service;

import com.qf.pojo.Garden;
import com.qf.pojo.PageBean;

import java.awt.print.Book;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:52
 */
public interface GardenService {
    //查询全部
    List<Garden> selectAll();
    //添加一条数据
    int addOne(Garden garden);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Garden selectOne(int id);
    //修改一条数据
    int updateOne(Garden garden);
    //分页查询
    PageBean<Garden> selectByLimit(String keywords,String currentPage, String pageSize);
    //导入excel文件
    int addGardens(List<String[]> datas);
    //导出excel文件
    List<String[]> exportBooks();
}
