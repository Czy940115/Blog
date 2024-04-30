package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: RoutersVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVO {
    List<MenuVO> menus;
}
