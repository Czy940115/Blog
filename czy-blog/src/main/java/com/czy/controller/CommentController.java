package com.czy.controller;

import com.czy.constants.SystemConstants;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.AddCommentDto;
import com.czy.domain.entity.Comment;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.service.CommentService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: CommentController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Integer articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_ARTICLE, articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        if (SecurityUtils.getLoginUser() != null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("linkCommentList")
    @ApiOperation(value = "友链评论链表", notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_LINK, null, pageNum, pageSize);
    }
}
