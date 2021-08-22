package com.jzjr.springbootshiro.filter;

import com.alibaba.fastjson.JSON;
import com.jzjr.springbootshiro.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AnyRolesAuthorizationFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
//        Subject subject = getSubject(servletRequest, servletResponse);
        Subject subject = SecurityUtils.getSubject();
        Object principal1 = subject.getPrincipal();
        if (subject.isAuthenticated()) {
            Object principal = subject.getPrincipal();
            PrincipalCollection principals = subject.getPrincipals();
            String[] rolesArray = (String[]) o;
            //没有权限控制,可以访问
            if (null == rolesArray || rolesArray.length == 0) {
                return true;
            }
            //若当前用户的角色是rolesArray中其中一个，则有权限访问
            for (String role : rolesArray) {
                if (subject.hasRole(role)) {
                    return true;
                }
                return false;
            }
        }
            return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
        publicLoginErrorController(response, "没有权限访问，请联系系统管理员");
        return false;
    }

    public void publicLoginErrorController(ServletResponse response, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //解决中文乱码
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        try {
            httpServletResponse.getWriter().write(JSON.toJSONString(new Result<>().setCode(500).setMessage(message)));
        } catch (IOException e) {
            log.error("IOException\t" + e.getMessage());
        }
    }
}
