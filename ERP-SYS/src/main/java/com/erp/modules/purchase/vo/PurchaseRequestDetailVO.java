package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购申请明细视图对象
 */
@Data
public class PurchaseRequestDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 采购申请ID */
    private Long prId;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 数量 */
    private BigDecimal quantity;

    /** 单位 */
    private String unit;

    /** 预估单价 */
    private BigDecimal estimatedPrice;

    /** 预估金额 */
    private BigDecimal estimatedAmount;
}
