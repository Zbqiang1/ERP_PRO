package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审批日志实体
 * 对应表：t_approval_log
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_approval_log")
public class ApprovalLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 业务类型（如：purchase_order, sales_order） */
    private String businessType;

    /** 业务ID */
    private Long businessId;

    /** 业务单号 */
    private String businessNo;

    /** 审批节点（审批步骤名称） */
    private String approvalNode;

    /** 审批人ID */
    private String approverId;

    /** 审批结果（0-驳回，1-通过，2-转审） */
    private Integer approvalResult;

    /** 审批意见 */
    private String approvalComment;

    /** 创建时间（日志只写不更新） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
