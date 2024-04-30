package com.czy.runner;

import com.czy.constants.RedisConstans;
import com.czy.domain.entity.Article;
import com.czy.mapper.ArticleMapper;
import com.czy.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: MyCommandLineRunner
 * Package: com.czy.runner
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 1.从数据库中查询博客的浏览量
        List<Article> list = articleMapper.selectList(null);

        // 2.将查询到的浏览量，存入Redis
        Map<String, Integer> map = list.stream()
                .collect
                        (Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));

        redisCache.setCacheMap(RedisConstans.ARTICLE_VIEWCOUNT, map);

        // System.out.println("初始化完成了");
    }
}
