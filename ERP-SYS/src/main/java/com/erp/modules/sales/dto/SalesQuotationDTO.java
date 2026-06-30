package com.erp.modules.sales.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售报价新增/编辑DTO
 */
@Data
public class SalesQuotationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 报价单号 */
    private String quotationNo;

    /** 客户ID */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 报价日期 */
    @NotNull(message = "报价日期不能为空")
    private LocalDate quotationDate;

    /** 有效截止日期 */
    private LocalDate validUntil;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 状态（0-草稿 1-已发送 2-已确认 3-已关闭） */
    private Integer status;
}
