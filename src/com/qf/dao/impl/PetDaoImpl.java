package com.qf.dao.impl;

import com.qf.dao.PersonDao;
import com.qf.dao.PetDao;
import com.qf.pojo.Car;
import com.qf.pojo.Person;
import com.qf.pojo.Pet;
import com.qf.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 14:53
 */
public class PetDaoImpl implements PetDao {

    @Override
    public List<Pet> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from pet", new BeanListHandler<>(Pet.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(Pet pet) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into pet values(?,?,?,?,?,?,?,?)";
        Object[] args = {pet.getId(),pet.getImage(),pet.getPerson(),pet.getName(),pet.getColor(),pet.getInfo(),pet.getAdoptTime(),pet.getCreateTime()};
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
            return qr.update(JDBCUtil.getConnection(),"delete from pet where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Pet selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from pet where id = ?", new BeanHandler<>(Pet.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(Pet pet) {
        QueryRunner qr = new QueryRunner();
        String sql = "update pet set image = ?, person = ?, name = ?, color = ?, info = ?, adoptTime = ?, createTime = ? where id = ?";
        Object[] args = {pet.getImage(),pet.getPerson(),pet.getName(),pet.getColor(),pet.getInfo(),pet.getAdoptTime(),pet.getCreateTime(),pet.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Pet> selectByLimit(String keywords, int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from pet limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from pet where name like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(Pet.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "select count(*) from pet";
        try {
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
