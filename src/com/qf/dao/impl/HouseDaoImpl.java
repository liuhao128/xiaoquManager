package com.qf.dao.impl;

import com.qf.dao.HouseDao;
import com.qf.pojo.Garden;
import com.qf.pojo.House;
import com.qf.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/28 15:50
 */
public class HouseDaoImpl implements HouseDao {
    @Override
    public List<House> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from house", new BeanListHandler<>(House.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(House house) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into house values(?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {house.getId(),house.getGarden(),house.getBuildingName(),house.getCode(),house.getHouseName(),house.getHouseholderName(),house.getPhone(),house.getHouseNumber(),house.getUnit(),house.getFloor(),house.getDescription(),house.getTime()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteOne(int id) {
        QueryRunner qr = new QueryRunner();
        try {
            return qr.update(JDBCUtil.getConnection(),"delete from house where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public House selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from house where id = ?", new BeanHandler<>(House.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(House house) {
        QueryRunner qr = new QueryRunner();
        String sql = "update house set garden = ?, buildingName = ?, code = ?, houseName = ?, householderName = ?, phone = ?, houseNumber = ?, unit = ?, floor = ?, description = ?, time = ?  where id = ?";
        Object[] args = {house.getGarden(),house.getBuildingName(),house.getCode(),house.getHouseName(),house.getHouseholderName(),house.getPhone(),house.getHouseNumber(),house.getUnit(),house.getFloor(),house.getDescription(),house.getTime(),house.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<House> selectByLimit(String keywords, int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from house limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from house where houseName like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(House.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "select count(*) from house";
        try {
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
