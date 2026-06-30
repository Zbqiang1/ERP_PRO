package com.erp.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购订单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_purchase_order")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 采购订单号 */
    private String poNo;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称（历史快照） */
    private String supplierName;

    /** 关联采购申请ID */
    private Long prId;

    /** 订单日期 */
    private LocalDate orderDate;

    /** 交货日期 */
    private LocalDate deliveryDate;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 单据状态（0-草稿，1-已下达，2-部分收货，3-已收货，4-已关闭） */
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
