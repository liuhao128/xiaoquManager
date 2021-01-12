package com.qf.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 这个工具类依赖于commons-dbutils-1.7.jar和commons-beanutils-1.8.3.jar
 * 双层查询时，属性名可以一样，我做了别名处理，
 * 但是要求数据库的属性名不能以“QweQweQwe[数字]”开头，不然就有可能属性名冲突
 * 不过一般没事干不会拿这玩意开头吧 =v=
 *
 * @author RW daze
 * @date 2020/12/14 22:19
 */
public class QueryRunnerUtil {
    /**
     * 把Map<String, Object>类型映射为T类型
     *
     * @param c   映射类型的类对象
     * @param map 被映射的Map
     * @param <T> 泛型，指需要映射到的类型
     * @return 映射出的对象
     */
    private static <T> T reflectionObject(Class<T> c, Map<String, Object> map, List<String> ignore) {
        try {
            Map<String, Object> newMap = new HashMap<>(map);
            if (ignore != null) {
                for (String s : ignore) {
                    newMap.remove(s);
                }
            }
            T t = c.newInstance();
            ConvertUtils.register((aClass, o) -> {
                //自定义转换规则  将字符串类型的日期转换成Date
                if (o instanceof java.sql.Date) {
                    java.sql.Date date = (java.sql.Date) o;
                    return new java.util.Date(date.getTime());
                } else if (o instanceof java.sql.Timestamp) {
                    java.sql.Timestamp date = (java.sql.Timestamp) o;
                    return new java.util.Date(date.getTime());
                } else {
                    return null;
                }
            }, java.util.Date.class);
            BeanUtils.populate(t, newMap);
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把Map<String, Object>类型映射为T类型
     *
     * @param c   映射类型的类对象
     * @param map 被映射的Map
     * @param <T> 泛型，指需要映射到的类型
     * @return 映射出的对象
     */
    private static <T> T reflectionObject(Class<T> c, Map<String, Object> map, String prefix) {
        try {
            Map<String, Object> newMap = new HashMap<>(map.size());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().startsWith(prefix)) {
                    newMap.put(entry.getKey().substring(prefix.length()), entry.getValue());
                }
            }
            T t = c.newInstance();
            ConvertUtils.register((aClass, o) -> {
                //自定义转换规则  将字符串类型的日期转换成Date
                if (o instanceof java.sql.Date) {
                    java.sql.Date date = (java.sql.Date) o;
                    return new java.util.Date(date.getTime());
                } else if (o instanceof java.sql.Timestamp) {
                    java.sql.Timestamp date = (java.sql.Timestamp) o;
                    return new java.util.Date(date.getTime());
                } else {
                    return null;
                }
            }, java.util.Date.class);
            BeanUtils.populate(t, newMap);
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断类对象是否属于基础属性
     *
     * @param objectClass 目标类对象
     * @return 是否属于
     */
    private static boolean isBaseType(Class<?> objectClass) {
        return objectClass == Integer.class || objectClass == String.class || objectClass == Double.class || objectClass == java.sql.Date.class
                || objectClass == java.util.Date.class || objectClass == Float.class || objectClass == Long.class;
    }

    /**
     * 查询某一个数据库中具体的对象
     *
     * @param tableName    需要查询的表名
     * @param propertyName 查询的关键属性名
     * @param value        查询的关键属性的属性值
     * @param c            查询对象的类对象
     * @param connection   连接
     * @param <T>          查询对象的泛型
     * @return 查询出的对象
     * @throws SQLException sql语句错误的异常
     */
    public static <T> T queryRunnerOne(String tableName, String propertyName, Object value, Class<T> c, Connection connection) throws SQLException {
        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        Field[] fie = c.getDeclaredFields();
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            QueryName annotation = field.getAnnotation(QueryName.class);
            if (i != 0) {
                sql.append(",");
            }
            if (annotation != null) {
                sql.append(annotation.value()).append(" ").append(field.getName());
            } else {
                sql.append(field.getName());
            }
        }
        sql.append(" from ").append(tableName).append(" where ").append(propertyName).append("=?");
        if (connection != null) {
            return qr.query(connection, sql.toString(), new BeanHandler<>(c), value);
        }
        return null;
    }

    /**
     * 根据传入的对象和目标数据库，向目标数据库注入相应的属性
     *
     * @param o          需要存入数据库的对象
     * @param tableName  数据库的表名
     * @param connection 连接
     * @return 数据库修改的行数
     * @throws IllegalAccessException 反射非法访问权限异常
     * @throws SQLException           sql语句错误异常
     */
    public static int queryRunnerAdd(Object o, String tableName, Connection connection) throws IllegalAccessException, SQLException {
        if (o == null) {
            return -1;
        }
        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();
        Class<?> c = o.getClass();
        Field[] fie = c.getDeclaredFields();
        LinkedList<Object> args = new LinkedList<>();

        sql.append("insert into ").append(tableName).append(" (");
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (i != 0) {
                    sql.append(",");
                }
                if (annotation != null) {
                    sql.append(annotation.value());
                } else {
                    sql.append(field.getName());
                }
            }
        }
        sql.append(")");
        sql.append(" values(");
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Object obj = field.get(o);
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                if (obj instanceof java.util.Date) {
                    args.add(new java.sql.Date(((java.util.Date) obj).getTime()));
                } else {
                    args.add(obj);
                }
                if (i == 0) {
                    sql.append("?");
                } else {
                    sql.append(", ?");
                }
            }
        }
        sql.append(")");

        return qr.update(connection, sql.toString(), args.toArray());
    }

    /**
     * 根据传入的表名，属性名和属性，删除表中对应的元素
     *
     * @param tableName    数据库的表名
     * @param propertyName 要删除对象的关键属性名
     * @param value        要删除对象的关键属性值
     * @param connection   连接
     * @throws SQLException sql语句错误异常
     */
    public static int queryRunnerDel(String tableName, String propertyName, Object value, Connection connection) throws SQLException {
        QueryRunner qr = new QueryRunner();
        String sql = "delete from " + tableName + " where " + propertyName + " = ?";
        Object[] args = {value};

        return qr.update(connection, sql, args);
    }

    /**
     * 根据传入的对象，表名，关键属性名，来更改表中的元素
     *
     * @param o               需要更改的元素的对象
     * @param tableName       数据库的表名
     * @param keyPropertyName 关键属性名，根据这个来找到相应的一条数据
     * @param connection      连接
     * @throws SQLException           sql语句错误异常
     * @throws IllegalAccessException 反射非法访问权限异常
     */
    public static int queryRunnerUpdate(Object o, String tableName, String keyPropertyName, Connection connection) throws SQLException, IllegalAccessException {
        if (o == null) {
            return -1;
        }
        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();
        Class<?> c = o.getClass();
        Field[] fie = c.getDeclaredFields();
        LinkedList<Object> args = new LinkedList<>();

        sql.append("update ").append(tableName).append(" set ");
        int index = -1;
        boolean sign = true;
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Object obj = field.get(o);
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                if (obj instanceof java.util.Date) {
                    args.add(new java.sql.Timestamp(((java.util.Date) obj).getTime()));
                } else {
                    args.add(obj);
                }
                String name = field.getName();
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (annotation != null) {
                    name = annotation.value();
                }
                if (name.equals(keyPropertyName)) {
                    index = i;
                } else {
                    if (sign) {
                        sign = false;
                    } else {
                        sql.append(", ");
                    }
                    sql.append(name).append(" = ? ");
                }
            }
        }
        if (index == -1) {
            return -1;
        }

        sql.append("where ").append(keyPropertyName).append(" = ?");
        Object remove = args.remove(index);
        args.add(remove);
        return qr.update(connection, sql.toString(), args.toArray());
    }

    /**
     * 根据传入的对象，表名，pageBean，类对象来查询并把资料存入pageBean
     *
     * @param o          查询的条件对象，若属性为null，则不作为查询条件
     * @param tableName  数据库的表名
     * @param pageBean   翻页类
     * @param c          类对象
     * @param <T>        类对象们的泛型
     * @param connection 连接
     * @throws SQLException           sql语句错误异常
     * @throws IllegalAccessException 非法访问权限异常
     */
    public static <T> void queryRunnerExact(T o, String tableName, IPage<T> pageBean, Class<T> c, Connection connection) throws SQLException, IllegalAccessException {
        if (o == null) {
            return;
        }

        if (connection == null) {
            return;
        }

        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();
        Field[] fie = c.getDeclaredFields();
        LinkedList<Object> args = new LinkedList<>();

        sql.append("select ");
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (i != 0) {
                    sql.append(",");
                }
                if (annotation != null) {
                    sql.append(annotation.value()).append(" ").append(field.getName());
                } else {
                    sql.append(field.getName());
                }
            }
        }
        sql.append(" from ").append(tableName).append(" where 1=1 ");
        for (Field field : fie) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                Object obj = field.get(o);
                if (obj != null) {
                    if (obj instanceof java.util.Date) {
                        args.add(new java.sql.Timestamp(((java.util.Date) obj).getTime()));
                    } else {
                        args.add(obj);
                    }
                    sql.append("and ");
                    String name = field.getName();
                    QueryName annotation = field.getAnnotation(QueryName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                    sql.append(name).append(" = ? ");
                }
            }
        }

        String querySql = sql + "limit " +
                (pageBean.getCurrentPage() - 1) * pageBean.getItemPerPage() +
                ", " + pageBean.getItemPerPage();
        List<T> result = qr.query(connection, querySql, new BeanListHandler<>(c), args.toArray());
        pageBean.setResult(result);
        int indexOfStar = sql.indexOf("*");
        String countSql = "Count(*)";
        sql.replace(indexOfStar, indexOfStar + 1, countSql);
        int query = qr.query(connection, sql.toString(), new ScalarHandler<Long>(), args.toArray()).intValue();
        pageBean.setItemCount(query);
        int pageCount;
        if (query % pageBean.getItemPerPage() == 0) {
            pageCount = query / pageBean.getItemPerPage();
        } else {
            pageCount = query / pageBean.getItemPerPage() + 1;
        }
        pageBean.setPageCount(pageCount);
    }

    /**
     * 根据传入的搜索名，表名，pageBean，类对象来查询并把资料存入pageBean
     *
     * @param searchName 搜索名
     * @param tableName  表名
     * @param pageBean   翻页类
     * @param c          类对象
     * @param connection 连接
     * @param <T>        类对象们的泛型
     * @throws SQLException sql语句错误异常
     */
    public static <T> void queryRunnerFuzzy(String searchName, String tableName, IPage<T> pageBean, Class<T> c, Connection connection) throws SQLException {
        if (connection == null) {
            return;
        }

        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();
        Field[] fie = c.getDeclaredFields();
        LinkedList<Object> args = new LinkedList<>();
        sql.append("select ");
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (i != 0) {
                    sql.append(",");
                }
                if (annotation != null) {
                    sql.append(annotation.value()).append(" ").append(field.getName());
                } else {
                    sql.append(field.getName());
                }
            }
        }
        sql.append(" from ").append(tableName).append(" where 1=0 ");
        Integer integer;
        try {
            integer = Integer.parseInt(searchName);
        } catch (NumberFormatException e) {
            integer = null;
        }
        for (Field field : fie) {
            QueryIgnore annotations = field.getAnnotation(QueryIgnore.class);
            if (null != annotations) {
                continue;
            }
            Class<?> type = field.getType();
            if (isBaseType(type)) {
                if (type.equals(Integer.class)) {
                    if (integer != null) {
                        args.add(integer);
                        sql.append("or ");
                        String name = field.getName();
                        QueryName annotation = field.getAnnotation(QueryName.class);
                        if (annotation != null) {
                            name = annotation.value();
                        }
                        sql.append(name).append(" like ? ");
                    }
                } else {
                    args.add("%" + searchName + "%");
                    sql.append("or ");
                    String name = field.getName();
                    QueryName annotation = field.getAnnotation(QueryName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                    sql.append(name).append(" like ? ");
                }
            }
        }

        String querySql = sql + "limit " +
                (pageBean.getCurrentPage() - 1) * pageBean.getItemPerPage() +
                ", " + pageBean.getItemPerPage();
        List<T> result = qr.query(connection, querySql, new BeanListHandler<>(c), args.toArray());
        pageBean.setResult(result);
        int indexOfStar = sql.indexOf("*");
        String countSql = "Count(*)";
        sql.replace(indexOfStar, indexOfStar + 1, countSql);
        int query = qr.query(connection, sql.toString(), new ScalarHandler<Long>(), args.toArray()).intValue();
        pageBean.setItemCount(query);
        int pageCount;
        if (query % pageBean.getItemPerPage() == 0) {
            pageCount = query / pageBean.getItemPerPage();
        } else {
            pageCount = query / pageBean.getItemPerPage() + 1;
        }
        pageBean.setPageCount(pageCount);
    }

    /**
     * 根据传入的对象，表名，替换属性名，替换值来向数据库中添加一条数据
     *
     * @param o          对象
     * @param tableName  表名
     * @param replace    属性替换键值对
     * @param connection 连接
     * @throws IllegalAccessException 非法访问权限异常
     * @throws SQLException           数据库语句异常
     */
    public static int queryRunnerAdd(Object o, String tableName, Map<String, Object> replace, Connection connection) throws IllegalAccessException, SQLException {
        if (o == null) {
            return -1;
        }
        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();
        Class<?> c = o.getClass();
        Field[] fie = c.getDeclaredFields();
        LinkedList<Object> args = new LinkedList<>();
        sql.append("insert into ").append(tableName).append(" (");
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (replace.containsKey(field.getName()) || isBaseType(type)) {
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (i != 0) {
                    sql.append(",");
                }
                if (annotation != null) {
                    sql.append(annotation.value());
                } else {
                    sql.append(field.getName());
                }
            }
        }
        sql.append(")");
        sql.append(" values(");
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (replace.containsKey(field.getName()) || isBaseType(type)) {
                Object obj;
                if (replace.containsKey(field.getName())) {
                    obj = replace.get(field.getName());
                } else {
                    obj = field.get(o);
                }
                if (obj instanceof java.util.Date) {
                    args.add(new java.sql.Timestamp(((java.util.Date) obj).getTime()));
                } else {
                    args.add(obj);
                }
                if (i == 0) {
                    sql.append("?");
                } else {
                    sql.append(", ?");
                }
            }
        }
        sql.append(")");

        return qr.update(connection, sql.toString(), args.toArray());
    }

    /**
     * 根据传入的对象，表名，关键属性名，来更改表中的元素
     *
     * @param o               对象
     * @param tableName       表名
     * @param keyPropertyName 关键属性名
     * @param replace         属性替换键值对
     * @param connection      连接
     * @throws SQLException           语句错误抛出异常
     * @throws IllegalAccessException 访问权限异常
     */
    public static int queryRunnerUpdate(Object o, String tableName, String keyPropertyName, Map<String, Object> replace, Connection connection) throws SQLException, IllegalAccessException {
        if (o == null) {
            return -1;
        }

        if (connection == null) {
            return -1;
        }

        QueryRunner qr = new QueryRunner();
        StringBuilder sql = new StringBuilder();

        Class<?> c = o.getClass();
        Field[] fie = c.getDeclaredFields();
        LinkedList<Object> args = new LinkedList<>();

        sql.append("update ").append(tableName).append(" set ");
        int index = -1;
        boolean sign = true;
        for (int i = 0; i < fie.length; i++) {
            Field field = fie[i];
            field.setAccessible(true);
            Class<?> type = field.getType();
            String name = field.getName();
            if (replace.containsKey(name) || isBaseType(type)) {
                Object obj = field.get(o);
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (annotation != null) {
                    name = annotation.value();
                }
                if (name.equals(keyPropertyName)) {
                    index = i;
                    if (obj instanceof java.util.Date) {
                        args.add(new java.sql.Timestamp(((java.util.Date) obj).getTime()));
                    } else {
                        args.add(obj);
                    }

                } else if (replace.containsKey(name)) {
                    Object value = replace.get(name);
                    if (value instanceof java.util.Date) {
                        args.add(new java.sql.Timestamp(((java.util.Date) value).getTime()));
                    } else {
                        args.add(value);
                    }

                    if (sign) {
                        sign = false;
                    } else {
                        sql.append(", ");
                    }
                    sql.append(name).append(" = ? ");
                } else {
                    if (obj instanceof java.util.Date) {
                        args.add(new java.sql.Timestamp(((java.util.Date) obj).getTime()));
                    } else {
                        args.add(obj);
                    }

                    if (sign) {
                        sign = false;
                    } else {
                        sql.append(", ");
                    }
                    sql.append(name).append(" = ? ");
                }
            }
        }
        if (index == -1) {
            return -1;
        }

        sql.append("where ").append(keyPropertyName).append(" = ?");
        Object remove = args.remove(index);
        args.add(remove);
        return qr.update(connection, sql.toString(), args.toArray());
    }

    /**
     * javaBean双层精确查询，当这个javaBean有属性为null时，不作为查询条件
     * 如果这个属性有值时，则会根据这个值来进行查询。
     * 如果这个属性是另一个javaBean的类，则会根据这个类表的主键进行查询。
     *
     * @param o                     需要查询的javaBean
     * @param c                     这个javaBean的类对象
     * @param innerClass            在这个javaBean中，其他javaBean对象的类对象列表
     * @param tableNames            这个javaBean在数据库中的表名
     * @param outKeyPropertyNames   这个javaBean的数据库中，其他javaBean对象的表名
     * @param innerKeyPropertyNames 所有内部javaBean数据库的主键名
     * @param pageBean              翻页类
     * @param connection            连接
     * @param <T>                   javaBean的泛型
     * @throws SQLException           sql语句错误
     * @throws IllegalAccessException 非法访问权限错误
     * @throws NoSuchFieldException   没有这样的属性错误
     */
    public static <T> void queryRunnerExactDoubleDeck(T o, Class<T> c, List<Class<?>> innerClass, List<String> tableNames, List<String> outKeyPropertyNames,
                                                      List<String> innerKeyPropertyNames, IPage<T> pageBean, Connection connection)
            throws SQLException, IllegalAccessException, NoSuchFieldException {
        if (o == null) {
            return;
        }
        if (connection == null) {
            return;
        }
        if (tableNames.size() != innerClass.size() + 1 || innerKeyPropertyNames.size() != tableNames.size() - 1 || outKeyPropertyNames.size() != tableNames.size() - 1) {
            return;
        }
        QueryRunner qr = new QueryRunner();

        StringBuilder sql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();
        sql.append("select ");
        countSql.append("select Count(*) ");
        LinkedList<Object> args = new LinkedList<>();

        Field[] oClassFields = c.getDeclaredFields();
        boolean sign = true;
        for (Field field : oClassFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Class<?> type = field.getType();
            if (!innerClass.contains(type) && isBaseType(type)) {
                if (sign) {
                    sign = false;
                } else {
                    sql.append(",");
                }
                String name;
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (annotation != null) {
                    name = annotation.value();
                    sql.append("a.").append(name).append(" ").append(fieldName);
                } else {
                    sql.append("a.").append(fieldName);
                }
            } else if (innerClass.contains(type)) {
                int index = innerClass.indexOf(type);
                Field[] innerFields = type.getDeclaredFields();
                for (Field innerField : innerFields) {
                    if (isBaseType(innerField.getType())) {
                        if (sign) {
                            sign = false;
                        } else {
                            sql.append(",");
                        }

                        String name;
                        QueryName annotation = innerField.getAnnotation(QueryName.class);
                        if (annotation != null) {
                            name = annotation.value();
                            sql.append("a").append(index).append(".").append(name).append(" QweQweQweQwe").append(index).append(innerField.getName());
                        } else {
                            sql.append("a").append(index).append(".").append(innerField.getName()).append(" QweQweQweQwe").append(index).append(innerField.getName());
                        }
                    }
                }
            }
        }

        sql.append(" from ").append(tableNames.get(0)).append(" a ");
        countSql.append(" from ").append(tableNames.get(0)).append(" a ");
        int count = -2;
        for (String tableName : tableNames) {
            count++;
            if (count == -1) {
                continue;
            }
            sql.append("inner join ").append(tableName).append(" a").append(count).append(" ");
            countSql.append("inner join ").append(tableName).append(" a").append(count).append(" ");
        }

        sql.append("where ");
        countSql.append("where ");
        boolean sign2 = true;
        Iterator<String> innerIterator = innerKeyPropertyNames.iterator();
        Iterator<String> outIterator = outKeyPropertyNames.iterator();
        int count2 = 0;
        while (innerIterator.hasNext() && outIterator.hasNext()) {
            String innerKeyPropertyName = innerIterator.next();
            String outKeyPropertyName = outIterator.next();
            if (sign2) {
                sign2 = false;
            } else {
                sql.append("and ");
                countSql.append("and ");
            }
            sql.append("a.").append(innerKeyPropertyName).append("=a").append(count2).append(".").append(outKeyPropertyName).append(" ");
            countSql.append("a.").append(innerKeyPropertyName).append("=a").append(count2).append(".").append(outKeyPropertyName).append(" ");
            count2++;
        }
        int count3 = 0;
        Iterator<String> innerIterator2 = innerKeyPropertyNames.iterator();
        for (Field field : oClassFields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            Object o1 = field.get(o);
            if (o1 != null) {
                if (!innerClass.contains(type) && isBaseType(type)) {
                    String name = field.getName();
                    QueryName annotation = field.getAnnotation(QueryName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                    sql.append("and a.").append(name).append("=? ");
                    countSql.append("and a.").append(name).append("=? ");
                    args.add(o1);
                } else if (innerClass.contains(type)) {
                    String next = innerIterator2.next();
                    Field innerField = type.getDeclaredField(next);
                    innerField.setAccessible(true);
                    Object o2 = innerField.get(o1);
                    String name = field.getName();
                    QueryName annotation = field.getAnnotation(QueryName.class);
                    if (annotation != null) {
                        name = annotation.value();
                    }
                    sql.append("and a").append(count3).append(".").append(name).append("=? ");
                    countSql.append("and a").append(count3).append(".").append(name).append("=? ");
                    args.add(o2);
                    count3++;
                }
            }
        }

        sql.append("limit ").append((pageBean.getCurrentPage() - 1) * pageBean.getItemPerPage())
                .append(",").append(pageBean.getItemPerPage());
        List<Map<String, Object>> queryMap = qr.query(connection, sql.toString(), new MapListHandler(), args.toArray());
        LinkedList<T> linkedList = new LinkedList<>();
        for (Map<String, Object> map : queryMap) {
            int cnt = 0;
            T tInstance = reflectionObject(c, map, new LinkedList<>());
            Iterator<Class<?>> inIterator = innerClass.iterator();
            Iterator<String> outKeyIterator = outKeyPropertyNames.iterator();
            while (inIterator.hasNext()) {
                Class<?> next = inIterator.next();
                String next1 = outKeyIterator.next();
                Object o1 = reflectionObject(next, map, "QweQweQweQwe" + cnt);
                Field[] declaredFields = c.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    QueryName annotation = declaredField.getAnnotation(QueryName.class);
                    if (annotation != null && annotation.value().equals(next1)) {
                        declaredField.setAccessible(true);
                        declaredField.set(tInstance, o1);
                        break;
                    }
                }
                cnt++;
            }
            linkedList.add(tInstance);
        }
        pageBean.setResult(linkedList);
        int queryCount = qr.query(connection, countSql.toString(), new ScalarHandler<Long>(), args.toArray()).intValue();
        pageBean.setItemCount(queryCount);
        int pageCount;
        if (queryCount % pageBean.getItemPerPage() == 0) {
            pageCount = queryCount / pageBean.getItemPerPage();
        } else {
            pageCount = queryCount / pageBean.getItemPerPage() + 1;
        }
        pageBean.setPageCount(pageCount);
    }

    /**
     * 双层模糊查询，根据查询关键字来进行对象模糊查询
     *
     * @param search                查询关键字
     * @param c                     需要查询的类对象
     * @param innerClass            类对象内部的对象列表
     * @param tableNames            表名列表
     * @param outKeyPropertyNames   需要查询的类对象内部所有其它对象的关键字列表
     * @param innerKeyPropertyNames 其他类的主键列表
     * @param pageBean              翻阅类
     * @param connection            连接
     * @param <T>                   需要查询的类对象的泛型
     * @throws SQLException           查询语句异常
     * @throws IllegalAccessException 非法访问权限异常
     * @throws NoSuchFieldException   没有这样的属性异常
     */
    public static <T> void queryRunnerFuzzyDoubleDeck(String search, Class<T> c, List<Class<?>> innerClass, List<String> tableNames, List<String> outKeyPropertyNames,
                                                      List<String> innerKeyPropertyNames, IPage<T> pageBean, Connection connection)
            throws SQLException, IllegalAccessException, NoSuchFieldException {
        if (search == null) {
            search = "";
        }
        if (connection == null) {
            return;
        }
        if (tableNames.size() != innerClass.size() + 1 || innerKeyPropertyNames.size() != tableNames.size() - 1 || outKeyPropertyNames.size() != tableNames.size() - 1) {
            return;
        }
        Integer searchInt = null;
        try {
            searchInt = Integer.parseInt(search);
        } catch (NumberFormatException ignored) {
        }
        QueryRunner qr = new QueryRunner();

        StringBuilder sql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();
        sql.append("select ");
        countSql.append("select Count(*) ");
        LinkedList<Object> args = new LinkedList<>();

        Field[] oClassFields = c.getDeclaredFields();
        boolean sign = true;
        for (Field field : oClassFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Class<?> type = field.getType();
            if (!innerClass.contains(type) && isBaseType(type)) {
                if (sign) {
                    sign = false;
                } else {
                    sql.append(",");
                }
                String name;
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (annotation != null) {
                    name = annotation.value();
                    sql.append("a.").append(name).append(" ").append(fieldName);
                } else {
                    sql.append("a.").append(fieldName);
                }
            } else if (innerClass.contains(type)) {
                int index = innerClass.indexOf(type);
                Field[] innerFields = type.getDeclaredFields();
                for (Field innerField : innerFields) {
                    if (isBaseType(innerField.getType())) {
                        if (sign) {
                            sign = false;
                        } else {
                            sql.append(",");
                        }
                        String name;
                        QueryName annotation = innerField.getAnnotation(QueryName.class);
                        if (annotation != null) {
                            name = annotation.value();
                            sql.append("a").append(index).append(".").append(name).append(" QweQweQweQwe").append(index).append(innerField.getName());
                        } else {
                            sql.append("a").append(index).append(".").append(innerField.getName()).append(" QweQweQweQwe").append(index).append(innerField.getName());
                        }
                    }
                }
            }
        }

        sql.append(" from ").append(tableNames.get(0)).append(" a ");
        countSql.append(" from ").append(tableNames.get(0)).append(" a ");
        int count = -2;
        for (String tableName : tableNames) {
            count++;
            if (count == -1) {
                continue;
            }
            sql.append("inner join ").append(tableName).append(" a").append(count).append(" ");
            countSql.append("inner join ").append(tableName).append(" a").append(count).append(" ");
        }

        sql.append("where ");
        countSql.append("where ");
        boolean sign2 = true;
        Iterator<String> innerIterator = innerKeyPropertyNames.iterator();
        Iterator<String> outIterator = outKeyPropertyNames.iterator();
        int count2 = 0;
        while (innerIterator.hasNext() && outIterator.hasNext()) {
            String innerKeyPropertyName = innerIterator.next();
            String outKeyPropertyName = outIterator.next();
            if (sign2) {
                sign2 = false;
            } else {
                sql.append("and ");
                countSql.append("and ");
            }
            sql.append("a.").append(innerKeyPropertyName).append("=a").append(count2).append(".").append(outKeyPropertyName).append(" ");
            countSql.append("a.").append(innerKeyPropertyName).append("=a").append(count2).append(".").append(outKeyPropertyName).append(" ");
            count2++;
        }
        sql.append("and (");
        countSql.append("and (");
        boolean mySign = true;
        int count3 = 0;
        Iterator<String> innerIterator2 = innerKeyPropertyNames.iterator();
        for (Field field : oClassFields) {
            field.setAccessible(true);
            QueryIgnore annotations = field.getAnnotation(QueryIgnore.class);
            if (null != annotations) {
                continue;
            }
            Class<?> type = field.getType();
            if (!innerClass.contains(type) && isBaseType(type)) {
                if (mySign) {
                    mySign = false;
                } else {
                    sql.append("or ");
                    countSql.append("or ");
                }
                String name = field.getName();
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (annotation != null) {
                    name = annotation.value();
                }
                sql.append("a.").append(name).append(" like ? ");
                countSql.append("a.").append(name).append(" like ? ");
                if ((type == Integer.class || type == Long.class) && searchInt != null) {
                    args.add(searchInt);
                } else {
                    args.add("%" + search + "%");
                }
            } else if (innerClass.contains(type)) {
                String next = innerIterator2.next();
                Field innerField = type.getDeclaredField(next);
                innerField.setAccessible(true);
                if (mySign) {
                    mySign = false;
                } else {
                    sql.append("or ");
                    countSql.append("or ");
                }
                String name = field.getName();
                QueryName annotation = field.getAnnotation(QueryName.class);
                if (annotation != null) {
                    name = annotation.value();
                }
                sql.append("a").append(count3).append(".").append(name).append(" like ? ");
                countSql.append("a").append(count3).append(".").append(name).append(" like ? ");
                if ((type == Integer.class || type == Long.class) && searchInt != null) {
                    args.add(searchInt);
                } else {
                    args.add("%" + search + "%");
                }
                count3++;
            }
        }
        countSql.append(") ");
        sql.append(") ");

        sql.append("limit ").append((pageBean.getCurrentPage() - 1) * pageBean.getItemPerPage())
                .append(",").append(pageBean.getItemPerPage());
        List<Map<String, Object>> queryMap = qr.query(connection, sql.toString(), new MapListHandler(), args.toArray());
        LinkedList<T> linkedList = new LinkedList<>();
        for (Map<String, Object> map : queryMap) {
            int cnt = 0;
            T tInstance = reflectionObject(c, map, new LinkedList<>());
            Iterator<Class<?>> inIterator = innerClass.iterator();
            Iterator<String> outKeyIterator = outKeyPropertyNames.iterator();
            while (inIterator.hasNext()) {
                Class<?> next = inIterator.next();
                String next1 = outKeyIterator.next();
                Object o1 = reflectionObject(next, map, "QweQweQweQwe" + cnt);
                Field[] declaredFields = c.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    QueryName annotation = declaredField.getAnnotation(QueryName.class);
                    if (annotation != null && annotation.value().equals(next1)) {
                        declaredField.setAccessible(true);
                        declaredField.set(tInstance, o1);
                        break;
                    }
                }
                cnt++;
            }
            linkedList.add(tInstance);
        }
        pageBean.setResult(linkedList);
        int queryCount = qr.query(connection, countSql.toString(), new ScalarHandler<Long>(), args.toArray()).intValue();
        pageBean.setItemCount(queryCount);
        int pageCount;
        if (queryCount % pageBean.getItemPerPage() == 0) {
            pageCount = queryCount / pageBean.getItemPerPage();
        } else {
            pageCount = queryCount / pageBean.getItemPerPage() + 1;
        }
        pageBean.setPageCount(pageCount);
    }
}
