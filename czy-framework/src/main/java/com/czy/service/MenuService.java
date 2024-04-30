package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Menu;
import com.czy.domain.entity.User;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-04-22 17:11:22
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermissions(Long id);

    List<String> getRoles(Long id);

    ResponseResult getRouters(User user);

    ResponseResult likeList(String status, String menuName);

    ResponseResult deleteById(Long menuId);

    ResponseResult treeselect();

    ResponseResult roleMenuTreeselect(Long id);
}
