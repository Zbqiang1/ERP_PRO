package com.erp.modules.purchase.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 采购订单查询DTO
 */
@Data
public class PurchaseOrderQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 采购订单号 */
    private String poNo;

    /** 供应商名称 */
    private String supplierName;

    /** 状态 */
    private String status;

    /** 下单开始日期 */
    private String orderDateStart;

    /** 下单结束日期 */
    private String orderDateEnd;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
