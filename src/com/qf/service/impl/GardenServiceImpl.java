package com.qf.service.impl;

import com.qf.dao.GardenDao;
import com.qf.dao.impl.GardenDaoImpl;
import com.qf.pojo.Garden;
import com.qf.pojo.PageBean;
import com.qf.service.GardenService;

import java.awt.print.Book;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/26 9:33
 */
public class GardenServiceImpl implements GardenService {
    GardenDao gardenDao = new GardenDaoImpl();
    @Override
    public List<Garden> selectAll() {
        List<Garden> gardenList = gardenDao.selectAll();
        return gardenList;
    }

    @Override
    public int addOne(Garden garden) {
        int i = gardenDao.addOne(garden);
        return i;
    }

    @Override
    public int deleteOne(int id) {
        int i = gardenDao.deleteOne(id);
        return i;
    }

    @Override
    public Garden selectOne(int id) {
        Garden garden = gardenDao.selectOne(id);
        return garden;
    }

    @Override
    public int updateOne(Garden garden) {
        int i = gardenDao.updateOne(garden);
        return i;
    }

    @Override
    public PageBean<Garden> selectByLimit(String keywords,String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Garden> gardenList = gardenDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = gardenDao.count(keywords);
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Garden> pageBean = new PageBean<>(gardenList,total,pages,cPage,pSize);
        return pageBean;
    }

    public int addGardens(List<String[]> datas){
        if(datas == null){
            return 0;
        }
        for (String[] data : datas) {
            //创建Book对象
            Garden garden = new Garden();
            garden.setCode(data[1]);
            garden.setName(data[2]);
            garden.setAddress(data[3]);
            garden.setArea(Double.parseDouble(data[4]));
            garden.setBuilding(Integer.parseInt(data[5]));
            garden.setGreen(data[6]);
            garden.setImage(data[7]);
            try {
                garden.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(data[8]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //插入garden类
            gardenDao.addOne(garden);
        }
        return datas.size();
    }

    public List<String[]> exportBooks(){
        //1、查询所有的图书信息
        List<Garden> gardens = gardenDao.selectAll();
        //2、将图书信息转化成List<String[]>
        List<String[]> datas = new ArrayList<>();
        for (Garden garden : gardens) {
            //创建一个String数组
            //String[] titles = {"小区序号","小区编号","小区名称","小区地址","小区面积","小区楼栋","小区绿化","小区图片","创建时间"};
            String[] data = new String[9];
            data[0] = String.valueOf(garden.getId());
            data[1] = garden.getCode();
            data[2] = garden.getName();
            data[3] = garden.getAddress();
            data[4] = String.valueOf(garden.getArea());
            data[5] = String.valueOf(garden.getBuilding());
            data[6] = garden.getGreen();
            data[7] = "http://localhost:8080"+garden.getImage();
            data[8] = new SimpleDateFormat("yyyy-MM-dd").format(garden.getTime());
            datas.add(data);
        }
        return datas;
    }


}
