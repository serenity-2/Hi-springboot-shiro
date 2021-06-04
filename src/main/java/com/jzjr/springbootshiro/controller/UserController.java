package com.jzjr.springbootshiro.controller;

import com.alibaba.fastjson.JSON;
import com.jzjr.springbootshiro.entity.Result;
import com.jzjr.springbootshiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
            //生成jwt token
            String jwtToken = userService.generateToken(username);
            //将token放入请求头返回前端,header的key是自定义命名的
            response.setHeader("x-auth-token",jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new Result().setCode(500).setMessage("用户名或密码错误"));
        }
        return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功"));
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login.jsp";
    }

    @RequestMapping("/register")
    public String register(String username, String password) {
        if (ObjectUtils.isEmpty(username) && ObjectUtils.isEmpty(password)) {
            return "redirect:/register.jsp";
        }
        try {
            userService.register(username, password);
            return "redirect:/login.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register.jsp";
        }

    }
}
