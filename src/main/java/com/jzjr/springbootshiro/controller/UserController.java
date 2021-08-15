package com.jzjr.springbootshiro.controller;

import com.alibaba.fastjson.JSON;
import com.jzjr.springbootshiro.dto.MessageVerityDTO;
import com.jzjr.springbootshiro.entity.Result;
import com.jzjr.springbootshiro.entity.VerifyCodeToken;
import com.jzjr.springbootshiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
            Object principal2 = subject.getPrincipal();
            System.out.println(subject.isAuthenticated());
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

    @RequestMapping("/verityCode/login")
    @ResponseBody
    public Result verityCodeLogin(@RequestBody MessageVerityDTO messageVerityDTO) {
        VerifyCodeToken token = new VerifyCodeToken();
        token.setPhoneNumber(messageVerityDTO.getPhoneNumber());
        token.setVerityCode(messageVerityDTO.getVerityCode());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return new Result<>().setCode(200).setMessage("短信验证通过,登陆成功");
    }
}
