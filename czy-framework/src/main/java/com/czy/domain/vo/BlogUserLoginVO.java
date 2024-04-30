package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: BlogUserLoginVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogUserLoginVO {
    private String token;
    private UserInfoVO UserInfo;
}
