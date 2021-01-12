package com.qf.dao.impl;

import com.qf.dao.UserDao;
import com.qf.pojo.User;
import com.qf.util.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * 作者：SmallWood
 * 时间：2020/12/26 9:40
 */
public class UserDaoImpl implements UserDao {

    @Override
    public User selectOneByName(String username) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from user where username = ?",new BeanHandler<>(User.class),username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User selectOneById(int id) {
        QueryRunner qr = new QueryRunner(JDBCUtil.getDataSource());
        try {
            return qr.query("select * from user where id = ?",new BeanHandler<>(User.class),id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateOne(User user) {
        QueryRunner qr = new QueryRunner();
        String sql = "update user set username = ?, password = ?, sex = ?, phone = ?, email = ?, birthday = ? where id = ?";
        Object[] args = {user.getUsername(),user.getPassword(),user.getSex(),user.getPhone(),user.getEmail(),user.getBirthday(),user.getId()};
        try {
            return qr.update(JDBCUtil.getConnection(),sql,args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
