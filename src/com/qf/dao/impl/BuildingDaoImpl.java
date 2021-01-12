package com.qf.dao.impl;

import com.qf.dao.BuildingDao;
import com.qf.pojo.Building;
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
 * 时间：2020/12/29 14:40
 */
public class BuildingDaoImpl implements BuildingDao {
    @Override
    public List<Building> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from building", new BeanListHandler<>(Building.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(Building building) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into building values(?,?,?,?,?,?,?)";
        Object[] args = {building.getId(),building.getGarden(),building.getCode(),building.getName(),building.getNumber(),building.getDescription(),building.getTime()};
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
            return qr.update(JDBCUtil.getConnection(),"delete from building where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Building selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from building where id = ?", new BeanHandler<>(Building.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(Building building) {
        QueryRunner qr = new QueryRunner();
        String sql = "update building set garden = ?, code = ?, name = ?, number = ?, description = ?, time = ? where id = ?";
        Object[] args = {building.getGarden(),building.getCode(),building.getName(),building.getNumber(),building.getDescription(),building.getTime(),building.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Building> selectByLimit(String keywords, int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from building limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from building where name like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(Building.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "select count(*) from building";
        try {
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
