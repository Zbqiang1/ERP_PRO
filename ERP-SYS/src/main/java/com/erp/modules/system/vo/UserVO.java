package com.erp.modules.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户视图对象
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像 */
    private String avatar;

    /** 部门ID */
    private String deptId;

    /** 部门名称 */
    private String deptName;

    /** 状态（0-禁用 1-启用） */
    private Integer status;

    /** 角色名称列表 */
    private List<String> roleNames;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
