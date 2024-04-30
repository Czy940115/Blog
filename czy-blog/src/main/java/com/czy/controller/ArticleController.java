package com.czy.controller;

import com.czy.annotation.MySystemlog;
import com.czy.domain.ResponseResult;
import com.czy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ArticleController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;// 注入公共模块的ArticleService接口

    // 测试MyBatisPlus
    @GetMapping("/list")
    public String test(){
        return articleService.list().toString();
    }

    // 测试统一相应格式
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        // 查询热门文章，封装成ResponseResult格式
        return articleService.hotArticleList();
    }

    /**
     * 分类查询文章的列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult articleById(@PathVariable("id") Integer id){
        return articleService.articleById(id);
    }

    @PutMapping("updateViewCount/{id}")
    @MySystemlog(businessName = "根据文章id从mysql中查询文章")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
