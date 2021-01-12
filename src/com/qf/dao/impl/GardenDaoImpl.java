package com.qf.dao.impl;

import com.qf.dao.GardenDao;
import com.qf.pojo.Garden;
import com.qf.pojo.PageBean;
import com.qf.service.GardenService;
import com.qf.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.awt.print.Book;
import java.sql.SQLException;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/26 9:40
 */
public class GardenDaoImpl implements GardenDao {
    @Override
    public List<Garden> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from garden", new BeanListHandler<>(Garden.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(Garden garden) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into garden values(?,?,?,?,?,?,?,?,?)";
        Object[] args = {garden.getId(),garden.getCode(),garden.getName(),garden.getAddress(),garden.getArea(),garden.getBuilding(),garden.getGreen(),garden.getImage(),garden.getTime()};
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
            return qr.update(JDBCUtil.getConnection(),"delete from garden where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Garden selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from garden where id = ?", new BeanHandler<>(Garden.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(Garden garden) {
        QueryRunner qr = new QueryRunner();
        String sql = "update garden set code = ?, name = ?, address = ?, area = ?, building = ?, green = ?, image = ?, time = ? where id = ?";
        Object[] args = {garden.getCode(),garden.getName(),garden.getAddress(),garden.getArea(),garden.getBuilding(),garden.getGreen(),garden.getImage(),garden.getTime(),garden.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Garden> selectByLimit(String keywords,int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from garden limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from garden where name like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(Garden.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count(String keyword) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            String sql;
            if(keyword == null){
                sql = "select count(*) from garden";
                return qr.query(sql,new ScalarHandler<Long>()).intValue();
            }else{
                sql = "select count(*) from garden where name like ?";
                return qr.query(sql,new ScalarHandler<Long>(),"%"+keyword+"%").intValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Garden> selectLike(String name) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from garden where name like ?", new BeanListHandler<>(Garden.class),"%"+name+"%");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
