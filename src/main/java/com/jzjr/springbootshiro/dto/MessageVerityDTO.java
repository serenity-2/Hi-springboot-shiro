package com.jzjr.springbootshiro.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MessageVerityDTO {
    @NotEmpty(message = "手机号不能为空")
    String phoneNumber;
    @NotEmpty(message = "验证码不能为空")
    String verityCode;
    @NotEmpty(message = "用户openId不能为空")
    String wxOpenId;
}
