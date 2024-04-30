package com.czy.domain.vo;

import com.czy.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ClassName: UserChangeVO
 * Package: com.czy.domain.vo
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserChangeVO {
    private List<Long> roleIds;

    List<Role> roles;

    UserInfoVO user;
}
