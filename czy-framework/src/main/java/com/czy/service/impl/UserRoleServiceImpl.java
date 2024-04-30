package com.czy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.domain.entity.UserRole;
import com.czy.mapper.UserRoleMapper;
import com.czy.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-04-24 15:57:50
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
