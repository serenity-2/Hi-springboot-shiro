package com.jzjr.springbootshiro.common;

public enum ResultCode {
    SUCCESS(0, "操作成功"),
    ERROR(999999, "操作失败"),
    /**
     * shiro相关异常
     */
    SHIRO_AUTHORIZATION_ERROR(10000,"用户鉴权异常"),
    SHIRO_AUTHENTICATION_ERROR(10001,"用户认证异常");


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;


}
