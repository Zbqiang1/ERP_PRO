package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售报价视图对象
 */
@Data
public class SalesQuotationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 报价单号 */
    private String quotationNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 报价日期 */
    private LocalDate quotationDate;

    /** 有效截止日期 */
    private LocalDate validUntil;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 状态（0-草稿 1-已发送 2-已确认 3-已关闭） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
