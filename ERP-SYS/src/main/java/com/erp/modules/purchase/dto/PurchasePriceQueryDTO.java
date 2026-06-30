package com.erp.modules.purchase.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 采购价格查询DTO
 */
@Data
public class PurchasePriceQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供应商名称 */
    private String supplierName;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
