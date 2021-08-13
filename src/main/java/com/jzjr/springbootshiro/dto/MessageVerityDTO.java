package com.jzjr.springbootshiro.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MessageVerityDTO {
    @NotNull(message = "手机号不能为空")
    String phoneNumber;
    @NotNull(message = "验证码不能为空")
    String verityCode;
}
