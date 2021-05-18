package com.jzjr.springbootshiro.service;

import com.jzjr.springbootshiro.entity.User;

public interface UserService {
    void register(String username, String password);

    User findUserByUserName(String principal);
}
