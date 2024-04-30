package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.TagDto;
import com.czy.domain.entity.Tag;
import com.czy.domain.vo.PageVO;
import com.czy.domain.vo.TagVO;
import com.czy.mapper.TagMapper;
import com.czy.service.TagService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author ChenZiyun
 * @since 2024-04-22 10:31:01
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, TagDto tagDto) {
        // 1.根据传输的参数进行分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.hasText(tagDto.getName()), Tag::getName, tagDto.getName());
        queryWrapper.like(StringUtils.hasText(tagDto.getRemark()), Tag::getRemark, tagDto.getRemark());

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 2.封装返回
        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delTag(Integer id) {
        // 将逻辑删除字段设置为1
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Integer id) {
        Tag tag = getById(id);
        TagVO tagVO = BeanCopyUtils.copyBean(tag, TagVO.class);
        return ResponseResult.okResult(tagVO);
    }

    @Override
    public ResponseResult updateTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = list();
        List<TagVO> tagVOS = BeanCopyUtils.copyBeanList(tagList, TagVO.class);
        return ResponseResult.okResult(tagVOS);
    }
}
