package com.jzjr.springbootshiro.service;

import com.jzjr.springbootshiro.entity.Permission;
import com.jzjr.springbootshiro.entity.User;

import java.util.List;

public interface UserService {
    void register(String username, String password);

    User findUserByUserName(String principal);

    List<Permission> findPermissionsByRoleId(String id);

    String generateToken(String userName);
}
