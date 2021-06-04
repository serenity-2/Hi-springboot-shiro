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

public class DbShiroRealm extends AuthorizingRealm {

    protected UserService userService;

    public DbShiroRealm(UserService userService) {
        this.userService = userService;
    }

    /**
     *该方法返回true则执行该realm
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获取用户角色和权限封装到simpleAuthorizationInfo，shiro帮我们验证
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

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        //根据用户名查询用户
        User user = userService.findUserByUserName(principal);
        if (!ObjectUtils.isEmpty(user)){
            //密码的校验无需我们自己比较,shiro的凭证匹配器可以帮我们实现
            return new SimpleAuthenticationInfo(user.getUserName(),user.getPassWord(),ByteSource.Util.bytes(user.getSalt()),this.getName());
        }
        return null;
    }
}
