package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 客户信用视图对象
 */
@Data
public class CustomerCreditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 信用额度 */
    private BigDecimal creditLimit;

    /** 已用额度 */
    private BigDecimal usedCredit;

    /** 可用额度 */
    private BigDecimal availableCredit;

    /** 最近核验日期 */
    private LocalDate lastCheckDate;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
