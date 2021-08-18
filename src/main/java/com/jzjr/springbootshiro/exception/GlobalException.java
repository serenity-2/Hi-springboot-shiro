package com.jzjr.springbootshiro.exception;

import com.jzjr.springbootshiro.common.ResultCode;
import com.jzjr.springbootshiro.entity.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(BusinessException.class)
    public Result businessException(BusinessException e) {
        return new Result(e.getCode(),e.getMessage(),null);
    }

    @ExceptionHandler(AuthorizationException.class)
    public Result authorizationException(AuthorizationException e) {
        return new Result(ResultCode.SHIRO_AUTHORIZATION_ERROR.getCode(),e.getMessage(),null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result authenticationException(AuthenticationException e) {
        return new Result(ResultCode.SHIRO_AUTHENTICATION_ERROR.getCode(), "用户名或密码错误",null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new Result(400, result.get("errorMsg").toString(), result.get("errorList"));
    }

    /**
     * 获取校验错误信息
     * @param fieldErrors
     * @return
     */
    private Map<String, Object> getValidError(List<FieldError> fieldErrors) {
        Map<String, Object> result = new HashMap<>(16);
        List<String> errorList = new ArrayList<>();
        StringBuilder errorMsg = new StringBuilder("校验异常(ValidException):");
        for (FieldError error : fieldErrors) {
            errorList.add(error.getField() + "-" + error.getDefaultMessage());
            errorMsg.append(error.getField()).append("-").append(error.getDefaultMessage()).append(".");
        }
        result.put("errorList", errorList);
        result.put("errorMsg", errorMsg);
        return result;
    }
}