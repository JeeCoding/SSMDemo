package com.huzhFramework.ssmDemo.utils;

import com.huzhFramework.ssmDemo.entity.Dept;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.junit.Assert.*;

public class JdbcTestTest {

    @Test
    public void driverTest() throws Exception {
        JdbcTest jdbcTest = new JdbcTest();
        jdbcTest.driverTest();
    }

    @Test
    public void getConnection() throws Exception {
        JdbcTest jdbcTest = new JdbcTest();
        jdbcTest.getConnection();
    }

    @Test
    public void getConnection2() throws Exception {
        JdbcTest jdbcTest = new JdbcTest();
        jdbcTest.getConnection2();
    }

    @Test
    public void statementTest() throws Exception {
        String sql = "insert into k_dept(name, age , birdate) values ('小明','18','2027-04-23')";
        JdbcTest jdbcTest = new JdbcTest();
        jdbcTest.statementTest(sql);
    }

    @Test
    public void get() {
        JdbcTest jdbcTest = new JdbcTest();
        String sql = "SELECT * FROM k_dept where id = ?";
        Dept dept = jdbcTest.get(Dept.class, sql, 2);
        System.out.println(dept);
    }
}