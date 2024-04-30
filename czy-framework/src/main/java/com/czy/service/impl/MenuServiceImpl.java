package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.constants.SystemConstants;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Menu;
import com.czy.domain.entity.RoleMenu;
import com.czy.domain.entity.User;
import com.czy.domain.vo.MenuTreeVo;
import com.czy.domain.vo.MenuVO;
import com.czy.domain.vo.RoleMenuTreeSelectVO;
import com.czy.domain.vo.RoutersVO;
import com.czy.mapper.MenuMapper;
import com.czy.service.MenuService;
import com.czy.service.RoleMenuService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.SecurityUtils;
import com.czy.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-04-22 17:11:23
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<String> getPermissions(Long id) {
        MenuMapper mapper = getBaseMapper();
        // 1.用户是管理员
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(Menu::getStatus, SystemConstants.MENU_NORMAL);
            queryWrapper.in(Menu::getMenuType, "C", "F");

            List<Menu> menuList = list(queryWrapper);
            List<String> list = menuList.stream()
                    .filter(menu -> StringUtils.hasText(menu.getPerms()))
                    .map(menu -> menu.getPerms())
                    .collect(Collectors.toList());
            return list;
        }else {
            // 2.非管理员，需要查询用户合法的权限
            List<String> ret = mapper.getPermissions(id);
            return ret;
        }
    }

    @Override
    public List<String> getRoles(Long id) {
        MenuMapper mapper = getBaseMapper();
        // 1.用户是管理员，roles列表为admin
        List<String> ret = new ArrayList<>();
        if (SecurityUtils.isAdmin()){
            ret.add("admin");
            return ret;
        }else {
            // 2.非管理员，查询roles列表
            ret = mapper.getRoles(id);
            return ret;
        }
    }

    @Override
    public ResponseResult getRouters(User user) {
        MenuMapper mapper = getBaseMapper();

        List<MenuVO> menuVOList = null;
        // 1.获取用户id
        Long userId = user.getId();
        if (SecurityUtils.isAdmin()){
            // 如果是管理员需要查询所有的menu
            menuVOList = mapper.getAllRouters();
        }else {
            // 2.根据用户id查询获取用户的合法menus
            menuVOList = mapper.getRouters(userId);
        }


        // 3.返回带树形结构的menu信息 -- 填充子节点
        menuVOList = creatTreeRouters(menuVOList, 0L);

        return ResponseResult.okResult(new RoutersVO(menuVOList));
    }

    @Override
    public ResponseResult likeList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        queryWrapper.orderByAsc(Menu::getId, Menu::getMenuName);

        List<Menu> menuList = list(queryWrapper);
        List<MenuVO> menuVOList = BeanCopyUtils.copyBeanList(menuList, MenuVO.class);

        return ResponseResult.okResult(menuVOList);
    }

    @Override
    public ResponseResult deleteById(Long menuId) {
        // 1.查询当前menuId中是否存在子菜单 -- 当前menuId是否是某个菜单的parentId
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, menuId);
        int count = count(queryWrapper);
        if (count > 0){
            return ResponseResult.errorResult(500, "存在子菜单不允许删除");
        }

        // 2.不存在进行删除
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeselect() {
        MenuMapper mapper = getBaseMapper();
        List<MenuVO> allRouters = mapper.getAllRouters();
        List<Menu> menus= BeanCopyUtils.copyBeanList(allRouters, Menu.class);

        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);

        return ResponseResult.okResult(menuTreeVos);
    }

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        User user = new User();
        user.setId(id);
        RoutersVO routersVO = (RoutersVO) getRouters(user).getData();

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        List<Long> checkedKeys = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());

        List<Menu> menus = BeanCopyUtils.copyBeanList(routersVO.getMenus(), Menu.class);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVO roleMenuTreeSelectVO = new RoleMenuTreeSelectVO(checkedKeys, menuTreeVos);
        return ResponseResult.okResult(roleMenuTreeSelectVO);
    }

    private List<MenuVO> creatTreeRouters(List<MenuVO> menuVOList, long parentId) {
        List<MenuVO> finalMenuVOList = menuVOList;
        menuVOList = menuVOList.stream()
                .filter(menuVO -> menuVO.getParentId().equals(parentId))
                .map(menuVO -> menuVO.setChildren(getChildren(menuVO, finalMenuVOList)))
                .collect(Collectors.toList());

        return menuVOList;
    }

    /**
     * 获取当前VO的子节点
     *
     * @param menuVO
     * @param menuVOList
     * @return
     */
    private List<MenuVO> getChildren(MenuVO menuVO, List<MenuVO> menuVOList) {
        return menuVOList.stream()
                .filter(menuVO1 -> menuVO1.getParentId().equals(menuVO.getId()))
                .map(menuVO1 -> menuVO1.setChildren(getChildren(menuVO1, menuVOList)))
                .collect(Collectors.toList());
    }
}
