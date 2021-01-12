package com.qf.dao.impl;

import com.qf.dao.FreeDao;
import com.qf.pojo.Device;
import com.qf.pojo.Free;
import com.qf.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 14:51
 */
public class FreeDaoImpl implements FreeDao {
    @Override
    public List<Free> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from free", new BeanListHandler<>(Free.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(Free free) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into free values(?,?,?,?,?)";
        Object[] args = {free.getId(),free.getGarden(),free.getCode(),free.getName(),free.getTime()};
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
            return qr.update(JDBCUtil.getConnection(),"delete from free where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Free selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from free where id = ?", new BeanHandler<>(Free.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(Free free) {
        QueryRunner qr = new QueryRunner();
        String sql = "update free set garden = ?, code = ?, name = ?, time = ? where id = ?";
        Object[] args = {free.getGarden(),free.getCode(),free.getName(),free.getTime(),free.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Free> selectByLimit(String keywords, int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from free limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from free where name like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(Free.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "select count(*) from free";
        try {
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
