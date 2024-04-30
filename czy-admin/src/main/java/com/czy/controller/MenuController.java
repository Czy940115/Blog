package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Menu;
import com.czy.domain.vo.MenuVO;
import com.czy.service.MenuService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: MenuController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(String status, String menuName){
        return menuService.likeList(status, menuName);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{menuId}")
    public ResponseResult getById(@PathVariable Long menuId){
        Menu menu = menuService.getById(menuId);
        MenuVO menuVO = BeanCopyUtils.copyBean(menu, MenuVO.class);
        return ResponseResult.okResult(menuVO);
    }

    @PutMapping
    public ResponseResult put(@RequestBody Menu menu){
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteById(@PathVariable Long menuId){
        return menuService.deleteById(menuId);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
