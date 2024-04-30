package com.czy.service;

import com.czy.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: UploadService
 * Package: com.czy.service
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
public interface UploadService {
    ResponseResult upload(MultipartFile file);
}
