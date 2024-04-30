package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.TagDto;
import com.czy.domain.entity.Tag;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-04-22 10:31:01
 */
public interface TagService extends IService<Tag> {

    ResponseResult list(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult delTag(Integer id);

    ResponseResult getTagById(Integer id);

    ResponseResult updateTag(TagDto tagDto);

    ResponseResult listAllTag();
}
