package com.huzhFramework.ssmDemo.dao;

import com.huzhFramework.ssmDemo.entity.Dept;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class DeptMapperTest {

    private ApplicationContext applicationContext;
    @Autowired
    private DeptMapper deptMapper;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
        deptMapper = applicationContext.getBean(DeptMapper.class);
    }

    @Test
    public void insert() throws Exception {
        Dept dept = new Dept();
        dept.setName("小明");
        dept.setAge(20);
        dept.setBirdate(new Date());
        int result = deptMapper.insert(dept);
        System.out.println(result);
        assert (result == 1);
    }
}