package com.erp.modules.hr.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 考勤视图对象
 */
@Data
public class AttendanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long employeeId;

    /** 员工编号 */
    private String employeeNo;

    /** 员工姓名 */
    private String employeeName;

    /** 考勤日期 */
    private LocalDate attendanceDate;

    /** 签到时间 */
    private LocalTime checkInTime;

    /** 签退时间 */
    private LocalTime checkOutTime;

    /** 状态（0-正常 1-迟到 2-早退 3-旷工 4-请假） */
    private Integer status;
}
