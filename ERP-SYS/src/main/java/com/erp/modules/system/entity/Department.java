package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门实体
 * 对应表：t_department
 */
@Data
@TableName("t_department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 上级部门ID（0为顶级部门） */
    private Long parentId;

    /** 部门名称 */
    private String deptName;

    /** 部门编码 */
    private String deptCode;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0-禁用 1-启用） */
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
