package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 销售订单明细视图对象
 */
@Data
public class SalesOrderDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 销售订单ID */
    private Long soId;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 数量 */
    private BigDecimal quantity;

    /** 已发数量 */
    private BigDecimal shippedQty;

    /** 单位 */
    private String unit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;
}
