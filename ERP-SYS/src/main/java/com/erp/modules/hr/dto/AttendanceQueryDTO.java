package com.erp.modules.hr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 考勤查询DTO
 */
@Data
public class AttendanceQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工姓名 */
    private String employeeName;

    /** 考勤开始日期 */
    private String attendanceDateStart;

    /** 考勤结束日期 */
    private String attendanceDateEnd;

    /** 状态 */
    private String status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
