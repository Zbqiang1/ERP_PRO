package com.erp.modules.purchase.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 采购退货查询DTO
 */
@Data
public class PurchaseReturnQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 退货单号 */
    private String returnNo;

    /** 采购订单号 */
    private String poNo;

    /** 供应商名称 */
    private String supplierName;

    /** 状态 */
    private Integer status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
