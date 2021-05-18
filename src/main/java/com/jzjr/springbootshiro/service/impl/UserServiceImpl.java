package com.jzjr.springbootshiro.service.impl;

import com.jzjr.springbootshiro.dao.UserDao;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void register(String username, String password) {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            String salt = UUID.randomUUID().toString().replaceAll("-","").substring(27);
            Md5Hash md5Hash = new Md5Hash(password,salt,1024);
            User user = new User().setUserName(username)
                    .setId(UUID.randomUUID().toString().replaceAll("-",""))
                    .setPassWord(md5Hash.toHex())
                    .setSalt(salt)
                    .setCreatedDate(new Date())
                    .setUpdatedDate(new Date());
            userDao.saveUser(user);

        }
    }

    @Override
    public User findUserByUserName(String principal) {
        return userDao.selectUserByUserName(principal);
    }
}
