package com.jzjr.springbootshiro.exception;

import com.jzjr.springbootshiro.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public Result businessException(BusinessException e) {
        return new Result(e.getCode(),e.getMessage(),null);
    }
}