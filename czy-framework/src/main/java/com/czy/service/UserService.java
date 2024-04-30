package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.UserDto;
import com.czy.domain.entity.User;

import java.util.List;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-04-17 19:43:56
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listByPage(Integer pageNum, Integer pageSize, User user);

    ResponseResult add(UserDto userDto);

    ResponseResult deleteUser(List<Long> userIds);

    ResponseResult getUserById(Long userId);

    ResponseResult updateUser(UserDto userDto);

}
