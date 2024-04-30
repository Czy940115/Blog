package com.czy.handler.execption;

import com.czy.domain.ResponseResult;
import com.czy.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.czy.handler.execption
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 处理自定义异常
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemException(SystemException e){
        // 打印异常信息
        log.error("出现了异常 {}", e);
        // 封装异常信息
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    // 处理SpringSecurity的权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult handleAccessDeniedException(AccessDeniedException e) {
        return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH.getCode(),e.getMessage());//枚举值是500
    }
    // 处理其他异常信息
    public ResponseResult exceptionHandler(Exception e){
        log.error("出现了异常! {}",e);

        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, e.getMessage());
    }
}
