package com.erp.modules.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织架构实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 上级组织ID（0为顶级组织） */
    private Long parentId;

    /** 组织名称 */
    private String orgName;

    /** 组织编码 */
    private String orgCode;

    /** 组织类型（0-公司，1-部门，2-小组） */
    private Integer orgType;

    /** 负责人ID */
    private String leaderId;

    /** 联系电话 */
    private String phone;

    /** 排序 */
    private Integer sortOrder;

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

    /** 逻辑删除（0-未删除，1-已删除） */
    @TableLogic
    private Integer deleted;
}
