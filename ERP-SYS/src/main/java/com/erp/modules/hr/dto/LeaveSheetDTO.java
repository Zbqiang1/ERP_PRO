package com.erp.modules.hr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 请假单新增/编辑DTO
 */
@Data
public class LeaveSheetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（更新时由Controller从路径参数设置） */
    private Long id;

    /** 请假单号 */
    private String leaveNo;

    /** 员工ID */
    @NotNull(message = "员工不能为空")
    private Long employeeId;

    /** 员工姓名 */
    private String employeeName;

    /** 请假类型（0-事假 1-病假 2-年假 3-婚假 4-产假） */
    @NotNull(message = "请假类型不能为空")
    private Integer leaveType;

    /** 开始时间 */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /** 结束时间 */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    /** 请假时长（小时） */
    private BigDecimal totalHours;

    /** 请假原因 */
    @NotBlank(message = "请假原因不能为空")
    private String reason;

    /** 状态（0-待审批 1-已通过 2-已驳回） */
    private Integer status;

    /** 审批人 */
    private String approver;
}
