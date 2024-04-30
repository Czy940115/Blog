package com.czy.controller;

import com.czy.annotation.MySystemlog;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.User;
import com.czy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: UserController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @MySystemlog(businessName = "查询用户信息")
    public ResponseResult userInfo(Integer userId){
        return userService.userInfo();
    }

    @PutMapping("userInfo")
    @MySystemlog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
