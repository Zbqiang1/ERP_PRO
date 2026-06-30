package com.erp.modules.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户新增/编辑DTO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 部门ID */
    private String deptId;

    /** 状态（0-禁用 1-启用） */
    private Integer status;

    /** 角色ID列表 */
    private List<Long> roleIds;
}
