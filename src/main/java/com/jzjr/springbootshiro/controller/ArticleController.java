package com.jzjr.springbootshiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    @RequestMapping("/list")
    @RequiresPermissions("article:list:select")
    public String articleList(){
        try {
            log.info("this is list");
            return "this is list";
        } catch (Exception e) {
            return "账号已过期，请重新登录";
        }
    }

    @RequestMapping("/admin")
    @RequiresPermissions("article:admin:select")
    public String coreSecret(){
        log.info("核心机密，请勿泄露");
        return "核心机密，请勿泄露";
    }
}
