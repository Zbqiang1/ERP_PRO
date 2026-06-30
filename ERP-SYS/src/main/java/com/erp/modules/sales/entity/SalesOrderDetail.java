package com.erp.modules.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 销售订单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sales_order_detail")
public class SalesOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 销售订单主表ID */
    private Long soId;

    /** 物料ID */
    private Long materialId;

    /** 订单数量 */
    private BigDecimal quantity;

    /** 已发货数量 */
    private BigDecimal shippedQty;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;

    /** 排序 */
    private Integer sortOrder;
}
