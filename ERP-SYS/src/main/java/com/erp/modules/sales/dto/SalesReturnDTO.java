package com.erp.modules.sales.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售退货新增/编辑DTO
 */
@Data
public class SalesReturnDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 退货单号 */
    private String returnNo;

    /** 销售订单ID */
    @NotNull(message = "销售订单不能为空")
    private Long soId;

    /** 销售订单号 */
    private String soNo;

    /** 客户ID */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /** 客户名称 */
    private String customerName;

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
