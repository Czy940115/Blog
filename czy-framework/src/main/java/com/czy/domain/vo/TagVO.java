package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: TagVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVO {
    private Long id;
    //标签名
    private String name;
    //备注
    private String remark;
}
