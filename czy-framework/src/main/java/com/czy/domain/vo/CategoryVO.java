package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: CategoryVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private Long id;
    private String name;
    //描述
    private String description;
}
