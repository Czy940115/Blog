package com.czy.service;

import com.czy.domain.ResponseResult;
import com.czy.domain.entity.User;

/**
 * ClassName: BlogLoginService
 * Package: com.czy.service
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
