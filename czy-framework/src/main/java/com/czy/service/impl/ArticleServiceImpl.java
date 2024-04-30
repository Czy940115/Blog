package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.constants.RedisConstans;
import com.czy.constants.SystemConstants;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.ArticleDto;
import com.czy.domain.entity.Article;
import com.czy.domain.entity.ArticleTag;
import com.czy.domain.entity.Category;
import com.czy.domain.vo.*;
import com.czy.mapper.ArticleMapper;
import com.czy.service.ArticleService;
import com.czy.service.ArticleTagService;
import com.czy.service.CategoryService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2024-04-15 11:38:07
 */
@Service("articleService")
// ServiceImpl是mybatisPlus官方提供的
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        // 查询条件
        // 1.查询的不能是草稿，status是0
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 2.安装浏览量进行排序，即按照ViewCount字段降序排列
        queryWrapper.orderByDesc(Article::getViewCount);
        // 3.最多只能查询出10条数据
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_CURRENT, SystemConstants.ARTICLE_STATUS_SIZE);
        // 获取查询结果
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        // 使用HotArticleVo对数据进行封装
//        List<HotArticleVO> articleVOs = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVO hotArticleVO = new HotArticleVO();
//            BeanUtils.copyProperties(article, hotArticleVO);
//            articleVOs.add(hotArticleVO);
//        }
        List<HotArticleVO> articleVOS = BeanCopyUtils.copyBeanList(articles, HotArticleVO.class);

        return ResponseResult.okResult(articleVOS);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 1.查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 1.1 分类页面需要查询对应分类下的文章列表。-- 是否具备categoryId
        lambdaQueryWrapper.eq((Objects.nonNull(categoryId)&&categoryId > 0), Article::getCategoryId, categoryId);
        // 1.2 只能查询正式发布的文章， -- 字段对比
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 1.3 置顶的文章要显示在最前面 -- 排序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        // 2.分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        // 3.封装返回结果
        // 3.1 将配置需要返回的信息简化
        List<Article> articles = page.getRecords();
        // 3.2 解决categoryName为空的情况
        // 方法一：for循环
//        for (Article article : articles){
//            article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
//        }

        // 方法二：使用stream流进行解决
        articles.stream()
                .map(article ->
                        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        List<ArticleVO> articleVOS = BeanCopyUtils.copyBeanList(articles, ArticleVO.class);

        PageVO pageVO = new PageVO(articleVOS, page.getTotal());

        // 3.3 将分页信息返回
        return ResponseResult.okResult(pageVO);
    }

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult articleById(Integer id) {
        // 1.根据id查询，文章的详细信息
        Article article = getById(id);

        // 从Redis中查询文章的浏览量
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        //把最后的查询结果封装成ArticleListVo(我们写的实体类)。BeanCopyUtils是我们写的工具类
        ArticleDetailVO articleDetailVO = BeanCopyUtils.copyBean(article, ArticleDetailVO.class);

        // 查询categoryName并进行赋值
        Category category = categoryService.getById(article.getCategoryId());
        articleDetailVO.setCategoryName(category.getName());
        return ResponseResult.okResult(articleDetailVO);
    }


    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(RedisConstans.ARTICLE_VIEWCOUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagService articleTagService;
    /**
     * 增加文章接口
     * @param articleDto
     * @return
     */
    @Override
    public ResponseResult addArticle(ArticleDto articleDto) {
        // 1.在数据库中增加文章
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        articleMapper.insert(article);
        // 2.增加文章对应的标签关系
        // 2.1获取存入数据库中article的id属性
        Long articleId = article.getId();
        // 2.2存入数据库
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleId, tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, Article article) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(article.getTitle()), Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()), Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        // 1.根据id查询文章内容
        Article article = getById(id);
        // 2.根据id查询文章标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> list = articleTagService.list(queryWrapper);
        List<Long> tags = list.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());

        // 3.封装返回
        ArticleAdminVO articleAdminVO = BeanCopyUtils.copyBean(article, ArticleAdminVO.class);
        articleAdminVO.setTags(tags);
        return ResponseResult.okResult(articleAdminVO);
    }

    @Override
    public ResponseResult updateArticle(ArticleAdminVO articleAdminVO) {
        // 1.更新文章表
        Article article = BeanCopyUtils.copyBean(articleAdminVO, Article.class);
        updateById(article);

        // 2.更新文章和标签的关联表
        // 移除原有的关联表信息
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);
        // 更新列表
        List<ArticleTag> articleTags = articleAdminVO.getTags().stream()
                .map(tagId -> new ArticleTag(articleAdminVO.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        // 1.删除文章
        removeById(id);
        // 2.删除文章和标签的对应关系
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        articleTagService.remove(queryWrapper);

        // 3.返回
        return ResponseResult.okResult();
    }
}
