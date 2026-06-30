package com.erp.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购订单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_purchase_order_detail")
public class PurchaseOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 采购订单主表ID */
    private Long poId;

    /** 物料ID */
    private Long materialId;

    /** 订单数量 */
    private BigDecimal quantity;

    /** 已收货数量 */
    private BigDecimal receivedQty;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;

    /** 排序 */
    private Integer sortOrder;
}
