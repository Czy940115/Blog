package com.czy.controller;

import com.czy.domain.ResponseResult;
import com.czy.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: UploadController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("upload")
    public ResponseResult upload(MultipartFile img){
        return uploadService.upload(img);
    }
}
