package com.jzjr.springbootshiro.shiro.realms;

import cn.hutool.core.bean.BeanUtil;
import com.jzjr.springbootshiro.entity.Permission;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.service.UserService;
import com.jzjr.springbootshiro.shiro.salt.MyByteSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import java.util.List;
@Slf4j
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("开始用户授权方法");
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.findUserByUserName(primaryPrincipal);
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (!BeanUtil.isEmpty(user)){
            user.getRoles().forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());
                List<Permission>permissions = userService.findPermissionsByRoleId(role.getId());
                permissions.forEach(permission ->{
                    simpleAuthorizationInfo.addStringPermission(permission.getName());
                });
            });
        }else {
        return null;
        }
                return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("开始用户认证方法");
        String principal = (String) authenticationToken.getPrincipal();
        User user = userService.findUserByUserName(principal);
        if (!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(user.getUserName(),user.getPassWord(),new MyByteSource(user.getSalt()),this
            .getName());
        }
        return null;
    }
}
