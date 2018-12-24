package com.huzhFramework.ssmDemo.utils;




import com.huzhFramework.ssmDemo.utils.JDBCDemo.ReflectionUtils;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * JDBC中包含了
 */
public class JdbcTest {

    public void driverTest() throws Exception {
        // 1. 创建一个 Driver 实现类的对象
        Driver dirver = new com.mysql.jdbc.Driver();

        // 2. 准备链接数据库的基本信息：url, user, password
        String url = "jdbc:mysql://localhost:3306/ssm_demo?useUnicode=true&characterEncoding=utf-8";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "123456");

        // 3. 调用 Driver 借口的 connect(url, info) 获取数据库连接
        Connection connection = dirver.connect(url, info);
        System.out.println(connection);
    }

    public Connection getConnection() throws Exception {
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

    public Connection getConnection2() throws Exception {

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

    public void statementTest(String sql) throws Exception {

        Connection conn = null;
        Statement statement = null;

        try {
            // 得到数据库连接
            conn = getConnection2();
            // 创建一个 statement 对象
            statement = conn.createStatement();
            // 操作SQL语句
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，并抛出异常
            try {
                if (statement != null)
                    statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.close();
            }
        }
    }

    public <T> T get(Class<T> clazz, String sql, Object... args) {
        T entity = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 得到 ResultSet 对象
            connection = getConnection2();
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
                for(Map.Entry<String, Object> entry:values.entrySet()){
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
     * 释放数据库资源的方法
     *
     * @return
     */
    public static void releaseDB(ResultSet resultSet, Statement statement, Connection connection) {
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

}
