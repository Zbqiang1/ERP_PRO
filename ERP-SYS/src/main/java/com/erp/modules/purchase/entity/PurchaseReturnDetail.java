package com.erp.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购退货明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_purchase_return_detail")
public class PurchaseReturnDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 退货主表ID */
    private Long returnId;

    /** 物料ID */
    private Long materialId;

    /** 退货数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;
}
