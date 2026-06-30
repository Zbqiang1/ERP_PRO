package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 仓库实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_warehouse")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 仓库编码 */
    private String warehouseCode;

    /** 仓库名称 */
    private String warehouseName;

    /** 仓库地址 */
    private String address;

    /** 负责人ID */
    private String managerId;

    /** 联系电话 */
    private String phone;

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
