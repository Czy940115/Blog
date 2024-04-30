package com.czy.filter;

import com.alibaba.fastjson.JSON;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.LoginUser;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.utils.JwtUtil;
import com.czy.utils.RedisCache;
import com.czy.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * ClassName: JwtAuthenticationTokenFilter
 * Package: com.czy.filter
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Component
//博客前台的登录认证过滤器。OncePerRequestFilter是springsecurity提供的类
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 1.获取token
        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 说明不需要登录校验直接放行
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // 2.解析token获取其中的userid
        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            // 当token过期或token被篡改就会进入下面那行的异常处理
            e.printStackTrace();
            // 需要把重新登录信息返回给前台
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }

        // 3.从redis中获取用户信息
        String userId = claims.getSubject();
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        if (Objects.isNull(loginUser)){
            // 需要把重新登录信息返回给前台
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }

        // 4.存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
