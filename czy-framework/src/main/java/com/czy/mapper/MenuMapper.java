package com.czy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czy.domain.entity.Menu;
import com.czy.domain.vo.MenuVO;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-22 17:13:30
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> getPermissions(Long id);

    List<String> getRoles(Long id);

    List<MenuVO> getRouters(Long userId);

    List<MenuVO> getAllRouters();

}
