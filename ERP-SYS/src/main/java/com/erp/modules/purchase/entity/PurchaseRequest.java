package com.erp.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购申请实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_purchase_request")
public class PurchaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 采购申请单号 */
    private String prNo;

    /** 申请部门ID */
    private String deptId;

    /** 申请人ID */
    private String applicantId;

    /** 申请日期 */
    private LocalDate applyDate;

    /** 期望交付日期 */
    private LocalDate expectedDeliveryDate;

    /** 单据状态（0-草稿，1-审批中，2-已审批，3-已转PO） */
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
