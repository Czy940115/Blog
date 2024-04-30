package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.domain.dto.ArticleDto;
import com.czy.domain.entity.Article;
import com.czy.domain.vo.ArticleAdminVO;
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
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, Article article){
        return articleService.listByPage(pageNum, pageSize, article);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleAdminVO articleAdminVO){
        return articleService.updateArticle(articleAdminVO);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable Long id){
        return articleService.deleteById(id);
    }
}
