package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购退货视图对象
 */
@Data
public class PurchaseReturnVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 退货单号 */
    private String returnNo;

    /** 采购订单ID */
    private Long poId;

    /** 采购订单号 */
    private String poNo;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 退货日期 */
    private LocalDate returnDate;

    /** 退货总金额 */
    private BigDecimal totalAmount;

    /** 退货原因 */
    private String returnReason;

    /** 状态 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
