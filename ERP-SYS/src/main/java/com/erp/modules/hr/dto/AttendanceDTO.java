package com.erp.modules.hr.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 考勤新增/编辑DTO
 */
@Data
public class AttendanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工ID */
    @NotNull(message = "员工不能为空")
    private Long employeeId;

    /** 员工编号 */
    private String employeeNo;

    /** 员工姓名 */
    private String employeeName;

    /** 考勤日期 */
    @NotNull(message = "考勤日期不能为空")
    private LocalDate attendanceDate;

    /** 签到时间 */
    private LocalTime checkInTime;

    /** 签退时间 */
    private LocalTime checkOutTime;

    /** 状态（0-正常 1-迟到 2-早退 3-旷工 4-请假） */
    private Integer status;
}
