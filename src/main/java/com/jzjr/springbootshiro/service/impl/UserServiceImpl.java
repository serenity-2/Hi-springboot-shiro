package com.jzjr.springbootshiro.service.impl;

import com.jzjr.springbootshiro.dao.UserDao;
import com.jzjr.springbootshiro.entity.Permission;
import com.jzjr.springbootshiro.entity.Role;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.service.UserService;
import com.jzjr.springbootshiro.utils.JWTUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void register(String username, String password) {
        if (!ObjectUtils.isEmpty(username) && !ObjectUtils.isEmpty(password)) {
            String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(27);
            Md5Hash md5Hash = new Md5Hash(password, salt, 1024);
            User user = new User().setUserName(username)
                    .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                    .setPassWord(md5Hash.toHex())
                    .setSalt(salt)
                    .setCreatedDate(new Date())
                    .setUpdatedDate(new Date());
            userDao.saveUser(user);
        }
    }

    @Override
    public User findUserByUserName(String principal) {
        HashMap<Object, Object> userMap = new HashMap<>();
        Permission permission = new Permission("1", "article:admin:*", "/article/admin/", new Date(), new Date());
        Role role = new Role("1", "admin", Arrays.asList(permission));
        List<Role> roles = Arrays.asList(role);
        User daisy = new User("1", "Daisy", "22f2d99d8d2d4d69bb9d457c747c8c33", "19aaf", roles, new Date(), new Date());
        userMap.put("Daisy",daisy);
        return (User) userMap.get(principal);
    }

    @Override
    public List<Permission> findPermissionsByRoleId(String id) {
        Permission permission = new Permission("1", "article:admin:*", "/article/admin/", new Date(), new Date());
        return Arrays.asList(permission);
//        return userDao.selectPermissionByRoleId(id);
    }

    @Override
    public String generateToken(String userName) {
        User user = new User("1", "Daisy", "22f2d99d8d2d4d69bb9d457c747c8c33", "19aaf", null, new Date(), new Date());
        String jwtToken = JWTUtils.sign(userName, user.getSalt());
        return jwtToken;
    }

}
