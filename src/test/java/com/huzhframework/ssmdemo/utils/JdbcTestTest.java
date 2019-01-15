package com.huzhframework.ssmdemo.utils;

import com.huzhframework.ssmdemo.entity.Dept;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class JdbcTestTest {

    @Test
    public void driverTest() throws Exception {
        JdbcTest jdbcTest = new JdbcTest();
    }

    @Test
    public void getConnection() throws Exception {
        JdbcTest jdbcTest = new JdbcTest();
    }

    @Test
    public void getConnection2() throws Exception {
        JdbcTest jdbcTest = new JdbcTest();
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
        String sql = "SELECT * FROM k_dept";
        Dept dept = jdbcTest.get(Dept.class, sql);
        System.out.println(dept);
    }

    @Test
    public void resultSetTest() throws Exception{
        JdbcTest jdbcTest = new JdbcTest();
        String sql = "SELECT * FROM k_dept";
        jdbcTest.resultSetTest(sql);
    }

    @Test
    public void preparedStatementTest() {
        JdbcTest jdbcTest = new JdbcTest();
        String sql = "insert into k_dept(name,age,birdate) values(?,?,?)";
        jdbcTest.preparedStatementTest(sql,"小明",19,new Date(new java.util.Date().getTime()));
    }

    @Test
    public void getForList() {
        JdbcTest jdbcTest = new JdbcTest();
        String sql = "SELECT * FROM k_dept";
        List<Dept> deptList = jdbcTest.getForList(Dept.class, sql);
        for (Dept dept: deptList){
            System.out.println(dept.getId());
        }
    }
}