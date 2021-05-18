package com.jzjr.springbootshiro.dao;

import com.jzjr.springbootshiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    void saveUser(User user);

    User selectUserByUserName(String principal);
}
