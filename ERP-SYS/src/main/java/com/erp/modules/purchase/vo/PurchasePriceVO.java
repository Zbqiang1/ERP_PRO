package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购价格视图对象
 */
@Data
public class PurchasePriceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 币种 */
    private String currency;

    /** 生效日期 */
    private LocalDate effectiveDate;

    /** 失效日期 */
    private LocalDate expireDate;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
