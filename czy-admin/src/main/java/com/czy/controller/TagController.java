package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.domain.dto.TagDto;
import com.czy.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: TagController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.list(pageNum, pageSize, tagDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delTag(@PathVariable("id") Integer id){
        return tagService.delTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable("id") Integer id){
        return tagService.getTagById(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.updateTag(tagDto);
    }

    @GetMapping("listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
