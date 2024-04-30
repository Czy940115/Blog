package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.UserDto;
import com.czy.domain.entity.Role;
import com.czy.domain.entity.User;
import com.czy.domain.entity.UserRole;
import com.czy.domain.vo.PageVO;
import com.czy.domain.vo.UserChangeVO;
import com.czy.domain.vo.UserInfoVO;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.handler.execption.SystemException;
import com.czy.mapper.UserMapper;
import com.czy.service.RoleService;
import com.czy.service.UserRoleService;
import com.czy.service.UserService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-04-17 19:43:56
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        // 获取登录用户id
        Long userId = SecurityUtils.getUserId();

        // 根据用户id，查询用户的详细信息
        User user = getById(userId);

        // 封装返回
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);
        return ResponseResult.okResult(userInfoVO);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        // 更新数据库中的用户信息
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        // 对提交的信息进行非空判断
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USEENAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        // 对数据库中数据进行查询是否重复
        if (userNameExit(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        // 保存到数据库中
        // 密码需要以密文的形式存入数据库中
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        queryWrapper.like(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        Page<User> page = new Page<>(pageNum, pageSize);

        page(page, queryWrapper);

        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;
    @Override
    public ResponseResult add(UserDto userDto) {
        // 1.对用户进行存储
        // 1.1 校验用户名是否存在
        if (!StringUtils.hasText(userDto.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USEENAME_NOT_NULL);
        }
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, user.getUserName());

        int count = count(queryWrapper);
        if (count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        // 1.2 对密码进行加密后存储
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);

        // 2.对用户和角色的关系进行存储
        List<Long> roleIds = userDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(List<Long> userIds) {
        if(userIds.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        // 1.删除用户信息
        removeByIds(userIds);

        return ResponseResult.okResult();
    }

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult getUserById(Long userId) {
        // 1.封装ids
        UserChangeVO userChangeVO = new UserChangeVO();
        ArrayList<Long> list = new ArrayList<>();
        list.add(userId);
        userChangeVO.setRoleIds(list);

        // 2.查询用户对应的角色
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoleList = userRoleService.list(queryWrapper);
        List<Long> roleIds = userRoleList.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());

        List<Role> roles = null;
        if (roleIds != null && roleIds.size() > 0)
            roles = roleService.listByIds(roleIds);
        userChangeVO.setRoles(roles);

        // 3.查询用户信息
        User user = getById(userId);
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);
        userChangeVO.setUser(userInfoVO);
        return ResponseResult.okResult(userChangeVO);
    }

    @Override
    public ResponseResult updateUser(UserDto userDto) {
        // 1.对用户进行存储
        // 1.1 校验用户名是否存在
        if (!StringUtils.hasText(userDto.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USEENAME_NOT_NULL);
        }
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, user.getUserName());

        userMapper.updateById(user);

        // 2.对用户和角色的关系进行存储
        List<Long> roleIds = userDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    private boolean userNameExit(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName, userName);
        int count = count(queryWrapper);
        return count > 0;
    }
}
