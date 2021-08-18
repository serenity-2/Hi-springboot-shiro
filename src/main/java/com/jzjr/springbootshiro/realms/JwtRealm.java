package com.jzjr.springbootshiro.realms;

import cn.hutool.core.bean.BeanUtil;
import com.jzjr.springbootshiro.config.JwtCredentialsMatcher;
import com.jzjr.springbootshiro.entity.JwtToken;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.service.UserService;
import com.jzjr.springbootshiro.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    protected UserService userService;
    public JwtRealm(UserService userService){
        this.userService = userService;
        this.setCredentialsMatcher(new JwtCredentialsMatcher());
    }
    /**
     * 添加JWTToken支持
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        log.info("support:{}",token instanceof JwtToken);
        return token instanceof JwtToken;
    }



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = jwtToken.getToken();
        User user = userService.findUserByUserName(JWTUtils.getUserName(token));
        if (BeanUtil.isEmpty(user)){
        throw new AuthenticationException("token过期，请重新登录");
        }
        return new SimpleAuthenticationInfo(user,user.getSalt(),this.getName());
    }
}
