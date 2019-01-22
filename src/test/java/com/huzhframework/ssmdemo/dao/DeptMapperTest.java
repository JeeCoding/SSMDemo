package com.huzhframework.ssmdemo.dao;

import com.huzhframework.ssmdemo.entity.Dept;
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
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
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

    @Test
    public void deleteByPrimaryKey() {
        int del = deptMapper.deleteByPrimaryKey(35);
        System.out.println(del);
    }


    @Test
    public void insertSelective() {
        Dept dept = new Dept();
        dept.setName("小明");
        dept.setAge(20);
        dept.setBirdate(new Date());
        int result = deptMapper.insertSelective(dept);
        System.out.println(result);
        assert (result == 1);
    }

    @Test
    public void selectByPrimaryKey() {
        Dept dept = deptMapper.selectByPrimaryKey(35);
        System.out.println(dept.getName());
    }

    @Test
    public void updateByPrimaryKeySelective() {
        Dept dept = new Dept();
        dept.setId(36);
        dept.setName("小明1");
        dept.setAge(21);
        dept.setBirdate(new Date());
        int result = deptMapper.updateByPrimaryKeySelective(dept);
        System.out.println(result);
        assert (result == 1);
    }

    @Test
    public void updateByPrimaryKey() {
        Dept dept = new Dept();
        dept.setId(36);
        dept.setName("小明2");
        dept.setAge(21);
        dept.setBirdate(new Date());
        int result = deptMapper.updateByPrimaryKey(dept);
        System.out.println(result);
        assert (result == 1);
    }
}