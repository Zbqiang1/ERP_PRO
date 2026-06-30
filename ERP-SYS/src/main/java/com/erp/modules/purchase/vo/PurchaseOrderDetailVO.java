package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购订单明细视图对象
 */
@Data
public class PurchaseOrderDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 采购订单ID */
    private Long poId;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 数量 */
    private BigDecimal quantity;

    /** 已收数量 */
    private BigDecimal receivedQty;

    /** 单位 */
    private String unit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;
}
