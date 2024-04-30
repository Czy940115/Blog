package com.czy.cronjob;

import com.czy.constants.RedisConstans;
import com.czy.domain.entity.Article;
import com.czy.service.ArticleService;
import com.czy.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: MyJob
 * Package: com.czy.cronjob
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Component
public class MyJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    // 用以做数据同步
    @Scheduled(cron = "* 0/10 * * * ?")//在哪个方法添加了@Scheduled注解，哪个方法就会定时去执行
    public void updateViewCount(){
        // 1.从Redis中查询浏览量
        Map<String, Integer> articleMap = redisCache.getCacheMap(RedisConstans.ARTICLE_VIEWCOUNT);

        List<Article> articleList = articleMap.entrySet()
                .stream().map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        // 2.更新到数据库中
        articleService.updateBatchById(articleList);

        // System.out.println("定时任务完成了");
    }
}
