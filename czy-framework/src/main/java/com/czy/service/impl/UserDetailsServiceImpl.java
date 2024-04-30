package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czy.constants.SystemConstants;
import com.czy.domain.entity.LoginUser;
import com.czy.domain.entity.User;
import com.czy.mapper.MenuMapper;
import com.czy.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * ClassName: UserDetailsServiceImpl
 * Package: com.czy.com.czy.com.czy.com.czy.com.czy.com.czy.service.impl
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);

        // 判断是否查询到用户，没有查询到抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        // 返回用户信息
        // TODO 查询权限信息封装
        // 如果用户是后台用户需要对权限信息进行封装
        if (SystemConstants.IS_ADMAIN.equals(user.getType())){
           List<String> permsList = menuMapper.getPermissions(user.getId());
           return new LoginUser(user, permsList);
        }
        return new LoginUser(user, null);
    }
}
