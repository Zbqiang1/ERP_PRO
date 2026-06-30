package com.erp.modules.sales.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户查询DTO
 */
@Data
public class CustomerQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 客户编码 */
    private String customerCode;

    /** 客户名称 */
    private String customerName;

    /** 状态（0-停用 1-启用） */
    private String status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
