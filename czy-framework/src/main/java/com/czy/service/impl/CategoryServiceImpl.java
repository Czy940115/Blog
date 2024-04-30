package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.constants.SystemConstants;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.CategoryDto;
import com.czy.domain.entity.Article;
import com.czy.domain.entity.Category;
import com.czy.domain.vo.CategoryVO;
import com.czy.domain.vo.PageVO;
import com.czy.mapper.CategoryMapper;
import com.czy.service.ArticleService;
import com.czy.service.CategoryService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-04-16 09:35:11
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        // 1.查询合理的文章列表 -- 文章状态为发布状态
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);

        // 2.根据文章列表，获取分类id -- 并去重
        Set<Long> categoryIdSet = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        // 3.根据分配id查询分类信息 -- 分类为合法的信息
        List<Category> categoryList = listByIds(categoryIdSet);

        // 3.1 收集未删除的分类信息
        categoryList = categoryList.stream()
                .filter(category -> SystemConstants.Category_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        // 3.2 屏蔽敏感信息
        List<CategoryVO> categoryVOS = BeanCopyUtils.copyBeanList(categoryList, CategoryVO.class);

        // 4.封装返回结果
        return ResponseResult.okResult(categoryVOS);
    }

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page);
        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult updateCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);

        CategoryVO categoryVO = BeanCopyUtils.copyBean(category, CategoryVO.class);
        return ResponseResult.okResult(categoryVO);
    }

    @Override
    public ResponseResult addCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }


}
