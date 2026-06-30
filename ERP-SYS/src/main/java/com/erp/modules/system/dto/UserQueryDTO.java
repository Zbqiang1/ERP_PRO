package com.erp.modules.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询DTO
 */
@Data
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户名 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 状态（0-禁用 1-启用） */
    private Integer status;

    /** 部门ID */
    private String deptId;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
