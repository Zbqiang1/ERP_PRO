package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 来料检验新增/编辑DTO
 */
@Data
public class IqcInspectionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 检验单号 */
    private String inspectionNo;

    /** 采购订单ID */
    @NotNull(message = "采购订单不能为空")
    private Long poId;

    /** 采购订单号 */
    private String poNo;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 检验员 */
    @NotBlank(message = "检验员不能为空")
    private String inspector;

    /** 检验日期 */
    @NotNull(message = "检验日期不能为空")
    private LocalDate inspectionDate;

    /** 检验结果（0-合格 1-不合格 2-让步接收） */
    @NotNull(message = "检验结果不能为空")
    private Integer inspectionResult;

    /** 备注 */
    private String remark;
}
