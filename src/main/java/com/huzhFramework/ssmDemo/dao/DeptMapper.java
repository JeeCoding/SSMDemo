package com.huzhFramework.ssmDemo.dao;

import com.huzhFramework.ssmDemo.entity.Dept;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dept record);

    int insertSelective(Dept record);

    Dept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);
}