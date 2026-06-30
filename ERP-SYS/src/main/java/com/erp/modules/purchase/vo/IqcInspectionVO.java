package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 来料检验视图对象
 */
@Data
public class IqcInspectionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 检验单号 */
    private String inspectionNo;

    /** 采购订单ID */
    private Long poId;

    /** 采购订单号 */
    private String poNo;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 检验员 */
    private String inspector;

    /** 检验日期 */
    private LocalDate inspectionDate;

    /** 检验结果（0-合格 1-不合格 2-让步接收） */
    private Integer inspectionResult;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
