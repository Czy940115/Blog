package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: LinkController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
