package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售发货视图对象
 */
@Data
public class SalesDeliveryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 发货单号 */
    private String deliveryNo;

    /** 销售订单ID */
    private Long soId;

    /** 销售订单号 */
    private String soNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 发货日期 */
    private LocalDate deliveryDate;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 状态（0-待发货 1-已发货 2-已签收） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
