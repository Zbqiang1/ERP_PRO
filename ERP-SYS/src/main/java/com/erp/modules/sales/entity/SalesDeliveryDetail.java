package com.erp.modules.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 销售发货单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sales_delivery_detail")
public class SalesDeliveryDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 发货单主表ID */
    private Long deliveryId;

    /** 物料ID */
    private Long materialId;

    /** 发货数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;
}
