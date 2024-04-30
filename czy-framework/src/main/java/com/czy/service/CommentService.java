package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-04-17 15:21:28
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Integer id, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
