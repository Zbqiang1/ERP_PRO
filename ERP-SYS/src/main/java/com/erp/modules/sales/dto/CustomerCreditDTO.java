package com.erp.modules.sales.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 客户信用新增/编辑DTO
 */
@Data
public class CustomerCreditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 客户ID */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 信用额度 */
    @NotNull(message = "信用额度不能为空")
    private BigDecimal creditLimit;

    /** 已用额度 */
    private BigDecimal usedCredit;

    /** 可用额度 */
    private BigDecimal availableCredit;

    /** 最近核验日期 */
    private LocalDate lastCheckDate;
}
