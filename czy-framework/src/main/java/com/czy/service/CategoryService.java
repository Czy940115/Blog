package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.CategoryDto;
import com.czy.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-04-16 09:35:11
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listByPage(Integer pageNum, Integer pageSize);

    ResponseResult updateCategory(CategoryDto categoryDto);

    ResponseResult deleteById(Long id);

    ResponseResult getCategoryById(Long id);

    ResponseResult addCategory(Category category);
}
