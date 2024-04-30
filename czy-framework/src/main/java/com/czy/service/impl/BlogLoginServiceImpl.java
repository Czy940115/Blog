package com.czy.service.impl;

import com.czy.domain.ResponseResult;
import com.czy.domain.entity.LoginUser;
import com.czy.domain.entity.User;
import com.czy.domain.vo.BlogUserLoginVO;
import com.czy.domain.vo.UserInfoVO;
import com.czy.service.BlogLoginService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.JwtUtil;
import com.czy.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * ClassName: BlogLoginServiceImpl
 * Package: com.czy.com.czy.com.czy.com.czy.com.czy.com.czy.service.impl
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取userId 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(userId.toString());

        // 把用户信息存入redis
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);

        // 把token和userInfo封装，返回
        // 把user转化为UserInfoVO
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);
        BlogUserLoginVO blogUserLoginVO = new BlogUserLoginVO(jwt, userInfoVO);

        return ResponseResult.okResult(blogUserLoginVO);
    }

    @Override
    public ResponseResult logout() {
        // 获取loginUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 获取userId
        Long userId = loginUser.getUser().getId();

        // 删除redis中的key
        redisCache.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult();
    }
}
