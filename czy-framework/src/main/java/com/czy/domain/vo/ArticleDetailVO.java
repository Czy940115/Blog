package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ClassName: ArticleDetailVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVO {
    //所属分类id
    private Long categoryId;
    // 所属分类名字
    private String categoryName;
    //文章内容
    private String content;
    // 文章创建时间
    private Date createTime;
    // 文章id
    private Long id;
    //是否允许评论 1是，0否
    private String isComment;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
