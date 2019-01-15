package com.huzhframework.ssmdemo.utils.jdbcdemo;

import org.apache.commons.beanutils.BeanUtils;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

/**
 * java.sql.Connection接口：一个连接
 */
public class JdbcUtils {

    /**
     * 查询一条记录，返回对应的对象
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> clazz, String sql, Object... args) {

        T entity = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 获取 Connection
            connection = getConnection();

            // 2. 获取 PreparedStatement
            preparedStatement = connection.prepareStatement(sql);

            // 3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            // 4. 进行查询，得到 ResultSet
            resultSet = preparedStatement.executeQuery();

            // 5. 若 ResultSet 中有记录，
            // 准备一个 Map<String, Object>: 键: 存放列的别名, 值: 存放列的值
            // 7. 处理 ResultSet, 使用 while 循环
            if (resultSet.next()) {
                Map<String, Object> values = new HashMap<String, Object>();

                // 6. 得到一个 ResultSetMetaData 对象
                ResultSetMetaData rsmd = resultSet.getMetaData();

                // 8. 由 ResultSetMetaData 对象得到结果集有多少列
                int columnCount = rsmd.getColumnCount();

                // 9. 由 ResultSetMetaData 得到每一列的别名, 由 Result 得到具体每一列的值
                for (int i = 0; i < columnCount; i++) {
                    String columnlabel = rsmd.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);

                    // 10. 填充 Map 对象
                    values.put(columnlabel, columnValue);
                }

                // 11. 用反射创建 Class 对应的对象
                entity = clazz.newInstance();

                // 12. 遍历 Map 对象, 用反射填充对象的属性值:
                // 属性名为 Map 中的 key, 属性值为 Map 中的 value
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    String propertyName = entry.getKey();
                    Object value = entry.getValue();

                    // ReflectionUtils.setFieldValue(entity, propertyName,
                    // value);
                    BeanUtils.setProperty(entity, propertyName, value);
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
     * 查询多条记录， 返回对应的对象集合
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static  <T> List<T> findList(Class<T> clazz, String sql, Object... args) {

        List<T> list = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 得到结果集
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            // 2. 处理结果集, 得到 Map 的 List, 其中一个 Map 对象
            // 就是一条记录. Map 的 key 为 reusltSet 中列的别名, Map 的 value
            // 为列的值.
            List<Map<String, Object>> values = handleResultSetToMapList(resultSet);

            // 3. 把 Map 的 List 转为 clazz 对应的 List
            // 其中 Map 的 key 即为 clazz 对应的对象的 propertyName,
            // 而 Map 的 value 即为 clazz 对应的对象的 propertyValue
            list = transfterMapListToBeanList(clazz, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseDB(resultSet, preparedStatement, connection);
        }

        return list;
    }

    /**
     * 如果 Map 集合大于 0 , 得到键、值复制给实体类的属性, 返回一个实体类的集合
     *
     * @param clazz
     * @param values
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static  <T> List<T> transfterMapListToBeanList(Class<T> clazz, List<Map<String, Object>> values)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<T> result = new ArrayList<T>();

        T bean = null;
        if (values.size() > 0) {
            for (Map<String, Object> m : values) {
                bean = clazz.newInstance();
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    BeanUtils.setProperty(bean, entry.getKey(), entry.getValue());
                }
                result.add(bean);
            }
        }
        return result;
    }

    /**
     * 处理结果集, 得到 Map 的一个 List, 其中一个 Map 对象对应一条记录(返回一个 Map 集合,存储每条数据)
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private static List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

        // ResultSetMetaData rsmd = resultSet.getMetaData();
        List<String> columnLabels = getColumnLabels(resultSet);

        Map<String, Object> map = null;
        while (resultSet.next()) {
            map = new HashMap<String, Object>();

            for (String columnLabel : columnLabels) {
                Object columnvalue = resultSet.getObject(columnLabel);
                map.put(columnLabel, columnvalue);
            }
            values.add(map);
        }
        return values;
    }

    /**
     * 获取结果集的 Columnlabel 对应的 List(返回数据库字段别名的集合)
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static List<String> getColumnLabels(ResultSet rs) throws SQLException {
        List<String> labels = new ArrayList<String>();

        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            labels.add(rsmd.getColumnLabel(i + 1));
        }
        return labels;
    }

    /**
     * sql更新操作
     *
     * @param sql
     * @param args
     */
    public static void update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
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

    /**
     * 连接数据库资源
     *
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {

        Properties properties = new Properties();
        InputStream inputStream = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        properties.load(inputStream);

        String driver = properties.getProperty("jdbc.driver");
        String jdbcUrl = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        //加载驱动
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        return connection;
    }
}
