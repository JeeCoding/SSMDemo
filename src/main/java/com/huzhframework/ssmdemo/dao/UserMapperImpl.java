package com.huzhframework.ssmdemo.dao;

import com.huzhframework.ssmdemo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserMapperImpl implements UserMapper {

    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    public int insert(User record) {
        return 0;
    }

    public int insertSelective(User record) {
        return 0;
    }

    public User selectByPrimaryKey(Long id) {
        return null;
    }

    public User selectByUsername(String username) {
        return null;
    }

    public int updateByPrimaryKeySelective(User record) {
        return 0;
    }

    public int updateByPrimaryKey(User record) {
        return 0;
    }
}
