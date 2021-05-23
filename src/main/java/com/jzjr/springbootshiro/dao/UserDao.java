package com.jzjr.springbootshiro.dao;

import com.jzjr.springbootshiro.entity.Permission;
import com.jzjr.springbootshiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    void saveUser(User user);

    User selectUserByUserName(String principal);

    List<Permission> selectPermissionByRoleId(String id);
}
