package com.huzhFramework.ssmDemo.utils;

import org.junit.Test;

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
    public void getConnection2() throws Exception{
        JdbcTest jdbcTest = new JdbcTest();
        jdbcTest.getConnection2();
    }
}