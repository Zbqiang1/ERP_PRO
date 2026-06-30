package com.erp.modules.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** JWT令牌 */
    private String token;

    /** 用户ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 头像 */
    private String avatar;
}
