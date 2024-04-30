package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * ClassName: CommentVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommentVO {
    private Long id;

    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //发根评论的userid
    private Long toCommentUserId;
    //发根评论的userName
    private String toCommentUserName;// **
    //回复目标评论id
    private Long toCommentId;
    //当前评论的创建人id
    private Long createBy;

    private Date createTime;

    //评论是谁发的
    private String username;// **

    private List children;
}
