package com.erp.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购申请明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_purchase_request_detail")
public class PurchaseRequestDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 采购申请主表ID */
    private Long prId;

    /** 物料ID */
    private Long materialId;

    /** 数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;

    /** 排序 */
    private Integer sortOrder;
}
