package com.huzhFramework.ssmDemo.utils;


import com.huzhFramework.ssmDemo.utils.JDBCDemo.JDBCUtils;
import com.huzhFramework.ssmDemo.utils.JDBCDemo.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * JDBC中包含了
 */
public class JdbcTest {

    /**
     * DatabaseMetaData 是描述 数据库 的元数据对象.
     * 可以由 Connection 得到.
     * 了解.
     */
    public void testDatabaseMetaData() {
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtils.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            // 可以得到数据库本身的一些基本信息
            // 1. 得到数据库的版本号
            int version = databaseMetaData.getDatabaseMajorVersion();
            System.out.println(version);

            // 2. 得到连接到数据库的用户名
            String user = databaseMetaData.getUserName();
            System.out.println(user);

            // 3. 得到 MySQL 中有哪些数据库
            resultSet = databaseMetaData.getCatalogs();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
            // ...........
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseDB(resultSet, null, connection);
        }

    }

    // 查询多条记录， 返回对应的对象集合
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {

        List<T> list = new ArrayList<T>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            Map<String, Object> map = null;
            while (resultSet.next()) {
                map = new HashMap<String, Object>();

                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object columnvalue = resultSet.getObject(i + 1);

                    map.put(columnLabel, columnvalue);
                }
                values.add(map);
            }

            T bean = null;
            if (values.size() > 0) {
                for(Map<String, Object> m: values){
                    bean = clazz.newInstance();
                    for(Map.Entry<String, Object> entry: m.entrySet()){
                        String propertyName = entry.getKey();
                        Object propertyValue = entry.getValue();

                        BeanUtils.setProperty(bean, propertyName, propertyValue);
                    }
                    list.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseDB(resultSet, preparedStatement, connection);
        }

        return list;
    }

    public void preparedStatementTest(String sql,Object...args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseDB(null, preparedStatement, connection);
        }
    }

    public void resultSetTest(String sql) throws Exception {

        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            statement = conn.createStatement();
            // 查询一条信息
            // String sql = "SELECT id,name,email,birth " + ""
            // + "FROM CUSTOMER WHERE ID = 3";
            // 查询多条信息
//            String sql = "SELECT id, name, email, birth from customer";


            rs = statement.executeQuery(sql);

            // 查询一条信息
            // if (rs.next()) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String age = rs.getString(3);
                Date birth = rs.getDate(4);
                System.out.print(id + " " + name + " " + age + " " + birth);
                System.out.println();
            }

        } catch (Exception e) {
        } finally {
            JDBCUtils.releaseDB(rs, statement, conn);
        }
    }


    public void statementTest(String sql) throws Exception {

        Connection conn = null;
        Statement statement = null;

        try {
            // 得到数据库连接
            conn = JDBCUtils.getConnection();
            // 创建一个 statement 对象
            statement = conn.createStatement();
            // 操作SQL语句
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseDB(null, statement, conn);
        }
    }

    public <T> T get(Class<T> clazz, String sql, Object... args) {
        T entity = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 得到 ResultSet 对象
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            // 2. 得到 ResultSetMetaData 对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // 3. 创建一个 Map<String, Object> 对象, 键: SQL 查询的列的别名,
            // 值: 列的值
            Map<String, Object> values = new HashMap<String, Object>();

            // 4. 处理结果集。 利用 ResultSetMetaData 填充 3 对应的 Map 对象
            while (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    String columnLabel = resultSetMetaData.getColumnLabel(i + 1);
                    Object columnvalue = resultSet.getObject(i + 1);

                    values.put(columnLabel, columnvalue);
                }
            }

            // 5. 若 Map 不为空集, 利用反射创建 clazz 对应的对象
            if (values.size() > 0) {
                entity = clazz.newInstance();
                // 6. 遍历 Map 对象, 利用 Map 对象, 利用反射为 Class 对象的对应属性赋值
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    String fieldName = entry.getKey();
                    Object value = entry.getValue();
                    ReflectionUtils.setFieldValue(entity, fieldName, value);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseDB(resultSet, preparedStatement, connection);
        }
        return entity;
    }

}
