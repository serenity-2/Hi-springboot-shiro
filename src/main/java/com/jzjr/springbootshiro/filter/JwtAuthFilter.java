package com.jzjr.springbootshiro.filter;

import com.alibaba.fastjson.JSON;
import com.jzjr.springbootshiro.entity.JwtToken;
import com.jzjr.springbootshiro.entity.Result;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.service.UserService;
import com.jzjr.springbootshiro.utils.JWTUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends AuthenticatingFilter {
    private UserService userService;

    public JwtAuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) //对于OPTION请求做拦截，不做token校验
            return false;

        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response){
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        request.setAttribute("jwtShiroFilter.FILTERED", true);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest var1, ServletResponse var2, Object var3){
        if (this.isLoginRequest(var1, var2))
            return true;
        boolean allowed = false;
        try {
            allowed = executeLogin(var1, var2);
        } catch (IllegalStateException e) {
            log.error("not found any token");
            publicLoginErrorController(var2,"账号已过期，请重新登录");
        } catch (Exception e) {
            log.error("error occurs when login");
            publicLoginErrorController(var2,"登录超时，请重新登录");
        }
        return allowed || super.isPermissive(var3);
    }

    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。这个方法返回null的话会直接抛出异常，进入isAccessAllowed（）的异常处理逻辑。
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
        if (!ObjectUtils.isEmpty(jwtToken) && !JWTUtils.isTokenExpired(jwtToken)) {
            return new JwtToken(jwtToken);
        }
        return null;
    }

    /**
     * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
     *
     * @param var1
     * @param var2
     * @return
     */
    protected boolean onAccessDenied(ServletRequest var1, ServletResponse var2) {
        HttpServletResponse response = WebUtils.toHttp(var2);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        fillCorsHeader(WebUtils.toHttp(var1), response);
        return false;
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        String newToken = null;
        if (token instanceof JwtToken) {
            JwtToken jwtToken = (JwtToken) token;
            User user = (User) subject.getPrincipal();
            boolean isTokenExpired = JWTUtils.isTokenExpired(jwtToken.getToken());
            if (isTokenExpired) {
                newToken = JWTUtils.sign(user.getUserName(), JWTUtils.SECRET);
            }
            if (!ObjectUtils.isEmpty(newToken)) {
                httpServletResponse.setHeader("x-auth-token", newToken);
            }
        }
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader("x-auth-token");
        return StringUtils.removeStart(header, "Bearer ");
    }

    public void publicLoginErrorController(ServletResponse response,String message){
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    //解决中文乱码
    httpServletResponse.setContentType("application/json;charset=UTF-8");
        try {
            httpServletResponse.getWriter().write(JSON.toJSONString(new Result<>().setCode(500).setMessage(message)));
        } catch (IOException e) {
            log.error("IOException\t"+e.getMessage());
        }
    }
}