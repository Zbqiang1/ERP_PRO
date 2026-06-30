package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售退货视图对象
 */
@Data
public class SalesReturnVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 退货单号 */
    private String returnNo;

    /** 销售订单ID */
    private Long soId;

    /** 销售订单号 */
    private String soNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    private String customerName;

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
