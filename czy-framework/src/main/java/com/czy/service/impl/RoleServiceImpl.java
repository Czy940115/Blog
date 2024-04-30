package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.RoleDto;
import com.czy.domain.entity.Role;
import com.czy.domain.entity.RoleMenu;
import com.czy.domain.vo.PageVO;
import com.czy.mapper.RoleMapper;
import com.czy.service.RoleMenuService;
import com.czy.service.RoleService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-04-24 11:24:47
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        queryWrapper.eq(StringUtils.hasText(status), Role::getStatus, status);
        queryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>();

        page(page, queryWrapper);

        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult changeStatus(RoleDto roleDto) {
        // 根据用户id修改用户状态
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setStatus(roleDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public ResponseResult add(RoleDto roleDto) {
        // 1.新增用户
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        roleMapper.insert(role);
        Long roleId = role.getId();

                // 2.新增用户和menu之间的关系
        // 2.1 将原有的关系删除
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(queryWrapper);

        // 2.2 添加新的关系
        List<Long> menuIds = roleDto.getMenuIds();
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(menuId -> new RoleMenu(roleId, menuId))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateRole(RoleDto roleDto) {
        // 1.更新用户
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        roleMapper.updateById(role);
        Long roleId = role.getId();

        // 2.新增用户和menu之间的关系
        // 2.1 将原有的关系删除
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(queryWrapper);

        // 2.2 添加新的关系
        List<Long> meniIds = roleDto.getMenuIds();
        List<RoleMenu> roleMenuList = meniIds.stream()
                .map(meniId -> new RoleMenu(roleId, meniId))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }
}
