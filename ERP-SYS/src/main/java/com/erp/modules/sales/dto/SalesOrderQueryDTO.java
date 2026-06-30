package com.erp.modules.sales.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 销售订单查询DTO
 */
@Data
public class SalesOrderQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 销售订单号 */
    private String soNo;

    /** 客户名称 */
    private String customerName;

    /** 状态 */
    private Integer status;

    /** 下单开始日期 */
    private String orderDateStart;

    /** 下单结束日期 */
    private String orderDateEnd;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
