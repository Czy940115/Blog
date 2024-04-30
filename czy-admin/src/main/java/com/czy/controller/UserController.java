package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.domain.dto.UserDto;
import com.czy.domain.entity.User;
import com.czy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: UserController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, User user){
        return userService.listByPage(pageNum, pageSize, user);
    }

    @PostMapping
    public ResponseResult add(@RequestBody UserDto userDto){
        return userService.add(userDto);
    }

    @DeleteMapping("/{userIds}")
    public ResponseResult deleteUser(@PathVariable List<Long> userIds){
        return userService.deleteUser(userIds);
    }

    @GetMapping("/{userId}")
    public ResponseResult getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping
    private ResponseResult update(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }
}
