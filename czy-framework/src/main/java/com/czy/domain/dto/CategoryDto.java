package com.czy.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: CategoryDto
 * Package: com.czy.domain.dto
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "后台分类dto")
public class CategoryDto {
    //分类名
    private String name;
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;
}
