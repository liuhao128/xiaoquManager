package com.qf.dao.impl;

import com.qf.dao.FreeDetailDao;
import com.qf.pojo.Device;
import com.qf.pojo.FreeDetail;
import com.qf.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 14:52
 */
public class FreeDetailDaoImpl implements FreeDetailDao {
    @Override
    public List<FreeDetail> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from free_detail", new BeanListHandler<>(FreeDetail.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(FreeDetail freeDetail) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into free_detail values(?,?,?,?,?,?,?,?,?)";
        Object[] args = {freeDetail.getId(),freeDetail.getGarden(),freeDetail.getProject(),freeDetail.getOwner(),freeDetail.getFromMoney(),freeDetail.getActualMoney(),freeDetail.getInfo(),freeDetail.getFreeTime(),freeDetail.getCreateTime()};
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
            return qr.update(JDBCUtil.getConnection(),"delete from free_detail where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public FreeDetail selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from free_detail where id = ?", new BeanHandler<>(FreeDetail.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(FreeDetail freeDetail) {
        QueryRunner qr = new QueryRunner();
        String sql = "update free_detail set garden = ?, project = ?, owner = ?, fromMoney = ?, actualMoney = ?, info = ?, freeTime = ?, createTime = ? where id = ?";
        Object[] args = {freeDetail.getGarden(),freeDetail.getProject(),freeDetail.getOwner(),freeDetail.getFromMoney(),freeDetail.getActualMoney(),freeDetail.getInfo(),freeDetail.getFreeTime(),freeDetail.getCreateTime(),freeDetail.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<FreeDetail> selectByLimit(String keywords, int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from free_detail limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from free_detail where project like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(FreeDetail.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "select count(*) from free_detail";
        try {
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
