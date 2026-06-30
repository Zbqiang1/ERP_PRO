package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 采购退货新增/编辑DTO
 */
@Data
public class PurchaseReturnDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 退货单号 */
    private String returnNo;

    /** 采购订单ID */
    @NotNull(message = "采购订单不能为空")
    private Long poId;

    /** 采购订单号 */
    private String poNo;

    /** 供应商ID */
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 退货日期 */
    @NotNull(message = "退货日期不能为空")
    private LocalDate returnDate;

    /** 退货总金额 */
    private BigDecimal totalAmount;

    /** 退货原因 */
    @NotBlank(message = "退货原因不能为空")
    private String returnReason;

    /** 状态 */
    private Integer status;
}
