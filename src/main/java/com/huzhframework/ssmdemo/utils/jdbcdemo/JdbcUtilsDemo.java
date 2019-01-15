package com.huzhframework.ssmdemo.utils.jdbcdemo;

import org.apache.commons.beanutils.BeanUtils;

import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class JdbcUtilsDemo {
    /**
     * DatabaseMetaData 是描述 数据库 的元数据对象.
     * 可以由 Connection 得到.类似ResultSetMetaData获取数据库信息
     */
    public void databaseMetaDataTest() {
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            connection = driverManagerConnection();
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
            releaseDB(resultSet, null, connection);
        }
    }

    /**
     * ResultSetMetaData:为resultSet的结果集的列信息（元数据）可以配合ResultSet,PreparedStatement查询数据
     * 查询多条数据
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> findListTest(Class<T> clazz, String sql, Object... args) {

        List<T> list = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = driverManagerConnection();
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
                for (Map<String, Object> m : values) {
                    bean = clazz.newInstance();
                    for (Map.Entry<String, Object> entry : m.entrySet()) {
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
            releaseDB(resultSet, preparedStatement, connection);
        }

        return list;
    }

    /**
     * ResultSetMetaData:为resultSet的结果集的列信息（元数据）可以配合ResultSet,PreparedStatement查询数据
     * 只能查一条信息
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getTest(Class<T> clazz, String sql, Object... args) {
        T entity = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 得到 ResultSet 对象
            connection = driverManagerConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            // 2. 得到 ResultSetMetaData 对象(获取数据字段信息)
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
            releaseDB(resultSet, preparedStatement, connection);
        }
        return entity;
    }

    /**
     * PreparedStatement实现更新操作
     * preparedStatement 可以有效防止SQL注入，解决了拼装SQL语句的繁琐方法
     *
     * @param sql
     * @param args
     */
    public void preparedStatementTest(String sql, Object... args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = driverManagerConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseDB(null, preparedStatement, connection);
        }
    }

    /**
     * ResultSet可以实现Statement的查询操作，较难处理多结果集
     *
     * @param sql
     * @throws Exception
     */
    public void resultSetTest(String sql) throws Exception {

        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            conn = driverManagerConnection();
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            // if (rs.next()) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String email = rs.getString(3);
                Date birth = rs.getDate(4);

                System.out.print(id + " " + name + " " + email + " " + birth);
                System.out.println();
            }

        } catch (Exception e) {
        } finally {
            releaseDB(rs, statement, conn);
        }
    }

    /**
     * Statement更新操作
     *
     * @param sql
     * @throws Exception
     */
    public void statementTest(String sql) throws Exception {

        Connection conn = null;
        Statement statement = null;

        try {
            // 得到数据库连接
            conn = driverManagerConnection();
            // 创建一个 statement 对象
            statement = conn.createStatement();
            // 操作SQL语句
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseDB(null, statement, conn);
        }
    }

    /**
     * 释放数据库资源的方法
     *
     * @return
     */
    public void releaseDB(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过DriverManager创建一个连接
     * 最常用的连接方式，比较成熟
     *
     * @return
     * @throws Exception
     */
    public Connection driverManagerConnection() throws Exception {

        Properties properties = new Properties();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");

        properties.load(inputStream);

        String driver = properties.getProperty("jdbc.driver");
        String jdbcUrl = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        Class.forName(driver);
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    /**
     * 通过Reflection和Driver创建一个连接
     *
     * @return
     * @throws Exception
     */
    public Connection reflectionDriverConnection() throws Exception {
        String driverClass = null;
        String jdbcUrl = null;
        String user = null;
        String password = null;

        // 读取类路径下的jdbc.properties 文件
        InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(in);

        driverClass = properties.getProperty("jdbc.driver");
        jdbcUrl = properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");

        // 通过反射创建 Driver 对象
        Driver driver = (Driver) Class.forName(driverClass).newInstance();

        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);

        // 通过 Driver 的 connect 方法连接数据库.
        Connection connection = driver.connect(jdbcUrl, info);

        return connection;
    }

    /*** 通过 Driver 创建一个连接
     *
     * @return
     * @throws Exception
     */
    public Connection driverConnection() throws Exception {
        // 1. 创建一个 Driver 实现类的对象
        Driver dirver = new com.mysql.jdbc.Driver();

        // 2. 准备链接数据库的基本信息：url, user, password
        String url = "jdbc:mysql://localhost:3306/ssm_demo?useUnicode=true&characterEncoding=utf-8";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "123456");

        // 3. 调用 Driver 借口的 connect(url, info) 获取数据库连接
        Connection connection = dirver.connect(url, info);
        return connection;
    }
}
