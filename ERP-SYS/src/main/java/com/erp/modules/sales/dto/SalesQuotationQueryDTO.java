package com.erp.modules.sales.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 销售报价查询DTO
 */
@Data
public class SalesQuotationQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 报价单号 */
    private String quotationNo;

    /** 客户名称 */
    private String customerName;

    /** 状态 */
    private String status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
