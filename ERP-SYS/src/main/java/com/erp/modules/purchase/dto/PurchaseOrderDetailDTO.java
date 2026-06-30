package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购订单明细DTO
 */
@Data
public class PurchaseOrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 物料ID */
    @NotNull(message = "物料不能为空")
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 数量 */
    @NotNull(message = "数量不能为空")
    private BigDecimal quantity;

    /** 单位 */
    private String unit;

    /** 单价 */
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;
}
