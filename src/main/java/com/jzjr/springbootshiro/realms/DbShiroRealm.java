package com.jzjr.springbootshiro.realms;

import cn.hutool.core.bean.BeanUtil;
import com.jzjr.springbootshiro.entity.Permission;
import com.jzjr.springbootshiro.entity.Role;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

public class DbShiroRealm extends AuthorizingRealm {

    protected UserService userService;

    public DbShiroRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (!BeanUtil.isEmpty(user)){
            List<Role> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)){
                roles.forEach(role -> {
                    simpleAuthorizationInfo.addRole(role.getName());
                    List<Permission> permissions = userService.findPermissionsByRoleId(role.getId());
                    permissions.forEach(permission ->{
                        simpleAuthorizationInfo.addStringPermission(permission.getName());
                    });
                });
            }
        }else {
            return null;
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        User user = userService.findUserByUserName(principal);
        if (!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(user.getUserName(),user.getPassWord(),ByteSource.Util.bytes(user.getSalt()),this.getName());
        }
        return null;
    }
}
