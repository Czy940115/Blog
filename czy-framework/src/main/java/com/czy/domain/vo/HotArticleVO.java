package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: HotArticleVO
 * Package: com.czy.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVO {
    private Long id;
    // 标题
    private String title;
    // 访问量
    private Long viewCount;
}
