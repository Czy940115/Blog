package com.czy.handler.security;

import com.alibaba.fastjson.JSON;
import com.czy.domain.ResponseResult;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: AuthenticationEntryPointImpl
 * Package: com.czy.handler.security
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Component
// 自定义认证失败的处理器
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 输出异常信息
        e.printStackTrace();

        ResponseResult result = null;
        // 判断登录时异常（’返回用户名或密码错误‘），还是没有登录就访问特特定的网页(返回'需要登录后访问')，还是其它情况(返回'出现错误')
        if (e instanceof BadCredentialsException){
            // 用户名密码错误
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), e.getMessage());
        }else if (e instanceof InsufficientAuthenticationException){
            // 权限不足
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证或授权失败");
        }

        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
