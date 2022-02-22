package com.lee.mht.system.exception.handler;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author FucXing
 * @date 2022/01/04 13:31
 **/
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResultObj unauthorizedException(UnauthorizedException e){
        log.error("UnauthorizedException,{},{}",e.getLocalizedMessage(),e);
        return new ResultObj(Constant.PERMISSION_ERROR_CODE,Constant.NOT_PERMISSION);
    }
}
