package com.erp.modules.hr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 请假单查询DTO
 */
@Data
public class LeaveSheetQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 请假单号 */
    private String leaveNo;

    /** 员工姓名 */
    private String employeeName;

    /** 请假类型 */
    private Integer leaveType;

    /** 状态 */
    private String status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
