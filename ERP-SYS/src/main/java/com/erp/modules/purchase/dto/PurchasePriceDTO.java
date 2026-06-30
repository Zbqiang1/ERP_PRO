package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 采购价格新增/编辑DTO
 */
@Data
public class PurchasePriceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供应商ID */
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 物料ID */
    @NotNull(message = "物料不能为空")
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 单价 */
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    /** 币种 */
    private String currency;

    /** 生效日期 */
    @NotNull(message = "生效日期不能为空")
    private LocalDate effectiveDate;

    /** 失效日期 */
    private LocalDate expireDate;
}
