package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.ArticleDto;
import com.czy.domain.entity.Article;
import com.czy.domain.vo.ArticleAdminVO;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2024-04-15 11:38:07
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult articleById(Integer id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto articleDto);

    ResponseResult listByPage(Integer pageNum, Integer pageSize, Article article);

    ResponseResult getArticleById(Long id);

    ResponseResult updateArticle(ArticleAdminVO articleAdminVO);

    ResponseResult deleteById(Long id);
}
