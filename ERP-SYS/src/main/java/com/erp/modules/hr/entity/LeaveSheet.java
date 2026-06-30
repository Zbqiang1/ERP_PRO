package com.erp.modules.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 请假单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_leave_sheet")
public class LeaveSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 请假单号 */
    private String leaveNo;

    /** 员工ID */
    private Long employeeId;

    /** 请假类型（0-事假，1-病假，2-年假，3-婚假，4-产假，5-调休） */
    private Integer leaveType;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 请假总时长 */
    private BigDecimal totalHours;

    /** 请假原因 */
    private String reason;

    /** 审批人ID */
    private String approverId;

    /** 审批时间 */
    private LocalDateTime approveTime;

    /** 单据状态（0-草稿，1-审批中，2-已批准，3-已驳回） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 创建人用户ID */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 修改人用户ID */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除（0-未删除，1-已删除） */
    @TableLogic
    private Integer deleted;
}
