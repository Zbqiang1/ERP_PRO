package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购申请明细DTO
 */
@Data
public class PurchaseRequestDetailDTO implements Serializable {

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

    /** 预估单价 */
    private BigDecimal estimatedPrice;

    /** 预估金额 */
    private BigDecimal estimatedAmount;
}
