package com.erp.modules.hr.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 请假单视图对象
 */
@Data
public class LeaveSheetVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 请假单号 */
    private String leaveNo;

    /** 员工ID */
    private Long employeeId;

    /** 员工姓名 */
    private String employeeName;

    /** 请假类型（0-事假 1-病假 2-年假 3-婚假 4-产假） */
    private Integer leaveType;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 请假时长（小时） */
    private BigDecimal totalHours;

    /** 请假原因 */
    private String reason;

    /** 状态（0-待审批 1-已通过 2-已驳回） */
    private Integer status;

    /** 审批人 */
    private String approver;

    /** 审批时间 */
    private LocalDateTime approveTime;

    /** 创建时间 */
    private LocalDateTime createTime;
}
