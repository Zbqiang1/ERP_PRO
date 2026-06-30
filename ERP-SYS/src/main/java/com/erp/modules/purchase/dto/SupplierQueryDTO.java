package com.erp.modules.purchase.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商查询DTO
 */
@Data
public class SupplierQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供应商编码 */
    private String supplierCode;

    /** 供应商名称 */
    private String supplierName;

    /** 状态（0-停用 1-启用） */
    private Integer status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
