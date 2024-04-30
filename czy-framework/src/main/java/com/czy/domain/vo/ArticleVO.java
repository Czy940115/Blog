package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ClassName: ArticleListVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {
    private String categoryName;
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Long likeCount;

    private Date createTime;
}
