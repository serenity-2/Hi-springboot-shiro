package com.jzjr.springbootshiro.entity;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class VerifyCodeToken implements AuthenticationToken {

    private User user;

    private String verityCode;

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public Object getCredentials() {
        return verityCode;
    }
}
