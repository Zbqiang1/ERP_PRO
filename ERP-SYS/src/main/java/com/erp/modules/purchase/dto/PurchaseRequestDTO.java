package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 采购申请新增/编辑DTO
 */
@Data
public class PurchaseRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 采购申请单号 */
    private String prNo;

    /** 部门ID */
    @NotNull(message = "部门不能为空")
    private String deptId;

    /** 部门名称 */
    private String deptName;

    /** 申请人 */
    @NotBlank(message = "申请人不能为空")
    private String applicant;

    /** 申请日期 */
    @NotNull(message = "申请日期不能为空")
    private LocalDate applyDate;

    /** 期望到货日期 */
    private LocalDate expectedDeliveryDate;

    /** 状态（0-草稿 1-已审批 2-已转订单 3-已驳回） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 采购申请明细列表 */
    private List<PurchaseRequestDetailDTO> details;
}
