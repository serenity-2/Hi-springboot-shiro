package com.jzjr.springbootshiro.exception;

public class BusinessException extends RuntimeException {
    private Integer code;

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException(Integer code,String message) {
        //在子类构造方法中调用父类构造方法，需要使用super(),且必须放到第一位，如果不写，默认会调用父类无参构造
        super(message);
        this.code = code;
    }

}
