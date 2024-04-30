package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.domain.entity.User;
import com.czy.domain.vo.AdminUserInfoVo;
import com.czy.domain.vo.UserInfoVO;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.handler.execption.SystemException;
import com.czy.service.LoginService;
import com.czy.service.MenuService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: BlogLoginController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @Autowired
    private MenuService menuService;

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        User user = SecurityUtils.getLoginUser().getUser();
        // 1.根据userId获取该用户的permission信息
        List<String> permissions = menuService.getPermissions(user.getId());

        // 2.根据userId查询roles角色数组
        List<String> roles = menuService.getRoles(user.getId());

        // 3.封装返回
        UserInfoVO userVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions, roles, userVO);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        User user = SecurityUtils.getLoginUser().getUser();

        return menuService.getRouters(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
