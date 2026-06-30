package com.erp.modules.sales.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户信用查询DTO
 */
@Data
public class CustomerCreditQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 客户名称 */
    private String customerName;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
