package com.erp.modules.sales.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 销售发货查询DTO
 */
@Data
public class SalesDeliveryQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 发货单号 */
    private String deliveryNo;

    /** 销售订单号 */
    private Long soId;

    /** 客户名称 */
    private String customerName;

    /** 状态 */
    private Integer status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
