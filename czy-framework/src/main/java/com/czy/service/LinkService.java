package com.czy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-04-16 17:07:42
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listByPage(Integer pageNum, Integer pageSize, String name, String status);
}
