package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购申请视图对象
 */
@Data
public class PurchaseRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 采购申请单号 */
    private String prNo;

    /** 部门ID */
    private String deptId;

    /** 部门名称 */
    private String deptName;

    /** 申请人 */
    private String applicant;

    /** 申请日期 */
    private LocalDate applyDate;

    /** 期望到货日期 */
    private LocalDate expectedDeliveryDate;

    /** 状态（0-草稿 1-已审批 2-已转订单 3-已驳回） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 采购申请明细列表 */
    private List<PurchaseRequestDetailVO> details;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
