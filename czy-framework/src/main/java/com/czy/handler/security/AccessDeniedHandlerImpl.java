package com.czy.handler.security;

import com.alibaba.fastjson.JSON;
import com.czy.domain.ResponseResult;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: AccessDeniedHandlerImpl
 * Package: com.czy.handler.security
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
// 自定义授权失败的处理器
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        // 输出异常信息
        e.printStackTrace();

        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);

        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
