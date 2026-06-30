package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关联实体
 * 对应表：t_user_role
 */
@Data
@TableName("t_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private String userId;

    /** 角色ID */
    private Long roleId;
}
