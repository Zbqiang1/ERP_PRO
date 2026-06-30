package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单实体
 * 对应表：t_menu
 */
@Data
@TableName("t_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 上级菜单ID（0为根菜单） */
    private Long parentId;

    /** 菜单名称 */
    private String menuName;

    /** 菜单类型（0-目录，1-菜单，2-按钮） */
    private Integer menuType;

    /** 路由路径 */
    private String path;

    /** 前端组件路径 */
    private String component;

    /** 权限标识 */
    private String perms;

    /** 图标 */
    private String icon;

    /** 排序 */
    private Integer sortOrder;

    /** 是否可见（0-隐藏，1-显示） */
    private Integer visible;

    /** 状态（0-禁用，1-启用） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 创建人用户ID */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 修改人用户ID */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标识（0-未删除 1-已删除） */
    @TableLogic
    private Integer deleted;
}
