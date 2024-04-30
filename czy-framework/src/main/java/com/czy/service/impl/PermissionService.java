package com.czy.service.impl;

import com.czy.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: PermissionService
 * Package: com.czy.com.czy.service.impl
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具备permission
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        // 如果是超级管理员，直接返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }

        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
