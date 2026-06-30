package com.erp.modules.purchase.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 采购申请查询DTO
 */
@Data
public class PurchaseRequestQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 采购申请单号 */
    private String prNo;

    /** 部门名称 */
    private String deptId;

    /** 申请人 */
    private String applicantId;

    /** 状态 */
    private String status;

    /** 申请开始日期 */
    private String applyDateStart;

    /** 申请结束日期 */
    private String applyDateEnd;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
