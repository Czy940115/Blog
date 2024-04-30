package com.czy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.domain.entity.RoleMenu;
import com.czy.mapper.RoleMenuMapper;
import com.czy.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2024-04-24 13:29:11
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
