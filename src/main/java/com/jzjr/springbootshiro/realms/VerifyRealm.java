package com.jzjr.springbootshiro.realms;

import cn.hutool.core.bean.BeanUtil;
import com.jzjr.springbootshiro.entity.Permission;
import com.jzjr.springbootshiro.entity.Role;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.entity.VerifyCodeToken;
import com.jzjr.springbootshiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class VerifyRealm extends AuthorizingRealm {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    protected UserService userService;

    public VerifyRealm(UserService userService) {
        this.userService = userService;
        this.setCredentialsMatcher(new SimpleCredentialsMatcher());
    }

    /**
     * 该方法返回true则执行该realm
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        log.info("support:{}", token instanceof VerifyCodeToken);
        return token instanceof VerifyCodeToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("开始用户授权");
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获取用户角色和权限封装到simpleAuthorizationInfo，shiro帮我们验证
        if (!BeanUtil.isEmpty(user)) {
            List<Role> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                roles.forEach(role -> {
                    simpleAuthorizationInfo.addRole(role.getName());
                    List<Permission> permissions = userService.findPermissionsByRoleId(role.getId());
                    permissions.forEach(permission -> {
                        simpleAuthorizationInfo.addStringPermission(permission.getName());
                    });
                });
                return simpleAuthorizationInfo;
            }

        }
        return null;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        VerifyCodeToken verifyCodeToken = (VerifyCodeToken) token;
        String phoneNumber = verifyCodeToken.getPhoneNumber();
        String verityCode = verifyCodeToken.getVerityCode();
        String realVerityCode = stringRedisTemplate.opsForValue().get(phoneNumber);
        User user = this.userService.selectUserByPhoneNumber(phoneNumber);
        return new SimpleAuthenticationInfo(user,realVerityCode,this.getName());
    }
}
