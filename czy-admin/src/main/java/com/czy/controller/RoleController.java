package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.domain.dto.RoleDto;
import com.czy.domain.entity.Role;
import com.czy.domain.vo.RoleVO;
import com.czy.service.RoleService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: RoleController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.listByPage(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto){
        return roleService.changeStatus(roleDto);
    }

    @PostMapping
    public ResponseResult add(@RequestBody RoleDto roleDto){
        return roleService.add(roleDto);
    }

    @GetMapping("/{roleId}")
    public ResponseResult getById(@PathVariable Long roleId){
        Role role = roleService.getById(roleId);
        RoleVO roleVO = BeanCopyUtils.copyBean(role, RoleVO.class);
        return ResponseResult.okResult(roleVO);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleDto roleDto){
        return roleService.updateRole(roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roleList = roleService.list();
        return ResponseResult.okResult(roleList);
    }
}
