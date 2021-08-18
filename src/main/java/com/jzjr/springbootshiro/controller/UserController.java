package com.jzjr.springbootshiro.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.jzjr.springbootshiro.dto.MessageVerityDTO;
import com.jzjr.springbootshiro.entity.Result;
import com.jzjr.springbootshiro.entity.User;
import com.jzjr.springbootshiro.entity.VerifyCodeToken;
import com.jzjr.springbootshiro.exception.BusinessException;
import com.jzjr.springbootshiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    public Result verityCodeLogin(@Valid @RequestBody MessageVerityDTO messageVerityDTO) {
        // 查询账号是否已被锁定
        Long expireTime = stringRedisTemplate.opsForValue().getOperations().getExpire("LOCKED" + messageVerityDTO.getPhoneNumber(), TimeUnit.MINUTES);
        //没有设置过期时间返回-1，不存在key返回-2
        if (expireTime != -2) {
            return new Result().setCode(200).setMessage("该账号被锁定请" + expireTime + "分钟后再试");
        }
        User user = userService.selectUserByPhoneNumber(messageVerityDTO.getPhoneNumber());
        VerifyCodeToken token = new VerifyCodeToken();
        token.setUser(user);
        token.setVerityCode(messageVerityDTO.getVerityCode());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            stringRedisTemplate.delete("LOCKED"+messageVerityDTO.getPhoneNumber());
            stringRedisTemplate.opsForValue().set(user.getUserName(),"0");
            //设置登录有效期，单位为毫秒,默认30分钟
            subject.getSession().setTimeout(60 * 1000);
        } catch (AuthenticationException e) {
            log.error("认证失败，请稍后重试:{}", e.getMessage());
            String loginCount = (String) stringRedisTemplate.opsForHash().get(user.getUserName(), "loginCount");
            Integer count = loginCount == null ? 0 : Integer.parseInt(loginCount);
            if (count < 5) {
                count = count + 1;
                stringRedisTemplate.opsForHash().put(user.getUserName(), "loginCount", String.valueOf(count));
                return new Result<>().setCode(500).setMessage("认证失败，请稍后重试");
            } else {
                //登录失败连续超过5次，该账号锁定20分钟，期间不能再次登录
                stringRedisTemplate.opsForValue().set("LOCKED" + messageVerityDTO.getPhoneNumber(), messageVerityDTO.getPhoneNumber(), 20, TimeUnit.MINUTES);
                return new Result<>().setCode(500).setMessage("该账户已经连续登录失败5次,请20分钟后重试");
            }
        }
            //判断用户是否是第一次登录，第一次登录还需人脸识别认证
            Date loginDate = user.getLoginDate();
            if (null == loginDate) {
                //需要人脸识别
                try {
                    faceIdentification();
                } catch (Exception e) {
                    return new Result<>().setCode(500).setMessage("登陆失败:"+e.getMessage());
                }
                //绑定用户微信openId
                user.setWxOpenId(messageVerityDTO.getWxOpenId());
                return new Result<>().setCode(200).setMessage("登陆成功");
            } else {
                //非第一次登录，需要验证openId
                if (!messageVerityDTO.getWxOpenId().equals(user.getWxOpenId())) {
                    //openId不一致，需要人脸识别
                    try {
                        faceIdentification();
                        userService.updateOpenIdByUserId(user.getId());
                    } catch (Exception ex) {
                        return new Result<>().setCode(500).setMessage("登陆失败:"+ex.getMessage());
                    }
                }
                return new Result<>().setCode(200).setMessage("登陆成功");
            }
        }

    /**
     * 人脸识别
     */
    @RequestMapping("/faceIdentification")
    public void faceIdentification() {
        //TODO
        throw new BusinessException(500,"人脸识别失败");
    }

    /**
     * 短信验证码
     */
    public Result messageVerity(@RequestBody MessageVerityDTO messageVerityDTO) {
        //查询当前用户是否存在
        User user = userService.selectUserByPhoneNumber(messageVerityDTO.getPhoneNumber());
        if (BeanUtil.isEmpty(user)) {
            return new Result().setCode(200).setMessage("当前用户不存在,请下载吉致管家注册登录");
        }
        //调用短信验证码接口 todo
        return null;
    }

}
