package com.qf.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:58
 */
public class JDBCUtil {
    private static DataSource dataSource;
    static{
        Properties properties = new Properties();
        //加载配置文件
        InputStream in = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(in);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static DataSource getDataSource(){
        return dataSource;
    }

    static ThreadLocal<Connection> tl = new ThreadLocal<>();
    public static Connection getConnection(){
        try {
            Connection connection = tl.get();
            if(connection == null){
                //从连接池中获取连接，并存到ThreadLocal中
                Connection conn = dataSource.getConnection();
                tl.set(conn);
                return conn;
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeAll(Connection conn, Statement statement , ResultSet rs){
        try {
            if(rs != null){
                rs.close();
            }
            if(statement != null){
                statement.close();
            }
            if(conn != null){
                //这个方法并没有关闭连接，只是放回到连接池中
                conn.close();
                tl.remove();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //封装事务的操作

    /**
     * 开启事务
     */
    public static void begin(){
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public static void commit(){
        Connection conn = getConnection();
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(conn,null,null);
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback(){
        Connection conn = getConnection();
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(conn,null,null);
        }
    }
}