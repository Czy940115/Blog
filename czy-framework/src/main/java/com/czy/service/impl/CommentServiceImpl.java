package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.constants.SystemConstants;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Comment;
import com.czy.domain.vo.CommentVO;
import com.czy.domain.vo.PageVO;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.handler.execption.SystemException;
import com.czy.mapper.CommentMapper;
import com.czy.service.CommentService;
import com.czy.service.UserService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-04-17 15:21:28
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Integer id, Integer pageNum, Integer pageSize) {
        // 根据文章id查询该文章下的所有评论信息 -- 分页查询
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 判断是否具备文章id
        queryWrapper.eq(SystemConstants.COMMENT_ARTICLE.equals(commentType), Comment::getArticleId, id);
        // 评论类型
        queryWrapper.eq(Comment::getType, commentType);
        // 根id为-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);

        Page page = new Page(pageNum, pageSize);

        page(page, queryWrapper);

        // 获取返回前端的信息
//        List<Comment> records = page.getRecords();
//        List<CommentVO> commentVOS = BeanCopyUtils.copyBeanList(records, CommentVO.class);

        // 增加返回缺少的username--评论是谁发的，toCommentUserName--发跟评论的名字
        List<CommentVO> commentVOS = toCommentVO((List<Comment>) page.getRecords());

        // 查询子评论
        commentVOS = commentVOS.stream()
                .map(commentVO -> commentVO.setChildren(getChildren(commentVO.getId()))
                ).collect(Collectors.toList());

        PageVO pageVO = new PageVO(commentVOS, page.getTotal());

        // 封装返回
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 增加字评论
        save(comment);
        return null;
    }

    // 查询子评论
    private List<CommentVO> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        // 对子评论按照时间顺序进行排序
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        return toCommentVO(comments);
    }

    private List<CommentVO> toCommentVO(List<Comment> records) {
        List<CommentVO> commentVOS = BeanCopyUtils.copyBeanList(records, CommentVO.class);

        // 设置发根评论的userName -- toCommentUserName
        // 设置username，当前发布人的姓名
        commentVOS = commentVOS.stream()
                // 设置回复评论的姓名 -- toCommentUserName
                .map(commentVO -> {
                    if (commentVO.getToCommentId() > 0)
                        return commentVO.setToCommentUserName(
                                userService.getById(getById(commentVO.getToCommentId()).getCreateBy()).getUserName());
                    return commentVO;
                })
                // 设置username，当前发布人的姓名
                .map(commentVO -> {
                    if (commentVO.getCreateBy() > 0)
                        return commentVO.setUsername(userService.getById(commentVO.getCreateBy()).getUserName());
                    return commentVO;
                })
                .collect(Collectors.toList());

        return commentVOS;
    }

}
