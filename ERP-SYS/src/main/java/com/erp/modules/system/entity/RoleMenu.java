package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单关联实体
 * 对应表：t_role_menu
 */
@Data
@TableName("t_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 角色ID */
    private Long roleId;

    /** 菜单ID */
    private Long menuId;
}
