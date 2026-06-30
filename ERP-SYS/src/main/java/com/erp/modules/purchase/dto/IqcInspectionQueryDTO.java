package com.erp.modules.purchase.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 来料检验查询DTO
 */
@Data
public class IqcInspectionQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 检验单号 */
    private String inspectionNo;

    /** 采购订单号 */
    private Long poId;

    /** 供应商名称 */
    private String supplierName;

    /** 检验结果 */
    private Integer inspectionResult;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
