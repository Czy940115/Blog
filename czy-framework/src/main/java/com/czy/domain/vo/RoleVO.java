package com.czy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: RoleVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVO {

    //角色ID
    private Long id;

    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //备注
    private String remark;
}
