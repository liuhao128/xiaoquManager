package com.qf.dao.impl;

import com.qf.dao.PersonDao;
import com.qf.pojo.Building;
import com.qf.pojo.Person;
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
public class PersonDaoImpl implements PersonDao {
    @Override
    public List<Person> selectAll() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from person", new BeanListHandler<>(Person.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addOne(Person person) {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into person values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {person.getId(),person.getGarden(),person.getHouse(),person.getName(),person.getImage(),person.getIdentity(),person.getPhone(),person.getJob(),person.getBirthday(),person.getSex(),person.getType(),person.getInfo(),person.getTime()};
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
            return qr.update(JDBCUtil.getConnection(),"delete from person where id = ?",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Person selectOne(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from person where id = ?", new BeanHandler<>(Person.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(Person person) {
        QueryRunner qr = new QueryRunner();
        String sql = "update person set garden = ?, house = ?, name = ?, image = ?, identity = ?, phone = ?, job = ?, birthday = ?, sex = ?, type = ?, info = ?,  time = ? where id = ?";
        Object[] args = {person.getGarden(),person.getHouse(),person.getName(),person.getImage(),person.getIdentity(),person.getPhone(),person.getJob(),person.getBirthday(),person.getSex(),person.getType(),person.getInfo(),person.getTime(),person.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Person> selectByLimit(String keywords, int currentPage, int pageSize) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "";
        Object[] args = null;
        if(keywords==null){
            sql =  "select * from person limit ?,?";
            args = new Object[]{(currentPage-1)*pageSize,pageSize};
        }else{
            sql = "select * from person where name like ?  limit ?,?";
            args = new Object[]{"%"+keywords+"%",(currentPage-1)*pageSize,pageSize};
        }
        try {
            return qr.query(sql, new BeanListHandler<>(Person.class),args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        String sql = "select count(*) from person";
        try {
            return qr.query(sql, new ScalarHandler<Long>()).intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
