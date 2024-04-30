package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.RoleDto;
import com.czy.domain.entity.Role;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-04-24 11:24:46
 */
public interface RoleService extends IService<Role> {

    ResponseResult listByPage(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(RoleDto roleDto);

    ResponseResult add(RoleDto roleDto);

    ResponseResult updateRole(RoleDto roleDto);
}
