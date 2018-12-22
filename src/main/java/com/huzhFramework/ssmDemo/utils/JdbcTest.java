package com.huzhFramework.ssmDemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

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

        String driver = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        Class.forName(driver);
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public void testStatement() throws Exception {

        Connection conn = null;
        Statement statement = null;

        try {
            // 得到数据库连接
            conn = getConnection2();
            // 准备SQL语句
            String sql = null;
            // sql = "INSERT INTO CUSTOMER (NAME,EMAIL,birth)
            // VALUES('qwer','abdce@qq.com','2027-04-23')";
            // System.out.println(sql);
            // sql = "DELETE FROM CUSTOMER WHERE ID = 1";
            sql = "update customer set name = 'TT' where id=4";
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

}
