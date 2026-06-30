package com.erp.modules.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单视图对象（树形结构）
 */
@Data
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 父级菜单ID */
    private Long parentId;

    /** 菜单名称 */
    private String menuName;

    /** 菜单类型（1-目录 2-菜单 3-按钮） */
    private Integer menuType;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 菜单图标 */
    private String icon;

    /** 排序 */
    private Integer sortOrder;

    /** 权限标识 */
    private String permissionCode;

    /** 是否可见（0-隐藏 1-显示） */
    private Integer visible;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 子菜单列表 */
    private List<MenuVO> children;
}
