package com.erp.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购退货实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_purchase_return")
public class PurchaseReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 退货单号 */
    private String returnNo;

    /** 关联采购订单ID */
    private Long poId;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称（历史快照） */
    private String supplierName;

    /** 退货日期 */
    private LocalDate returnDate;

    /** 退货总金额 */
    private BigDecimal totalAmount;

    /** 退货原因 */
    private String returnReason;

    /** 单据状态（0-草稿，1-已退货，2-已完成） */
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
