package com.czy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.constants.SystemConstants;
import com.czy.domain.ResponseResult;
import com.czy.domain.entity.Link;
import com.czy.domain.vo.LinkVO;
import com.czy.domain.vo.PageVO;
import com.czy.mapper.LinkMapper;
import com.czy.service.LinkService;
import com.czy.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-04-16 17:07:42
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 1.查询所有的链接信息 -- 状态合法
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getDelFlag, SystemConstants.Link_STATUS_NORMAL);

        List<Link> linkList = list(queryWrapper);
        // 2.封装返回
        List<LinkVO> linkVOS = BeanCopyUtils.copyBeanList(linkList, LinkVO.class);
        return ResponseResult.okResult(linkVOS);
    }

    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Link::getName, name);
        queryWrapper.eq(StringUtils.hasText(status), Link::getStatus, status);

        Page<Link> page = new Page<Link>();

        page(page, queryWrapper);

        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }
}
