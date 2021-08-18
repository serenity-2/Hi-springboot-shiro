package com.jzjr.springbootshiro.filter;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTION,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
