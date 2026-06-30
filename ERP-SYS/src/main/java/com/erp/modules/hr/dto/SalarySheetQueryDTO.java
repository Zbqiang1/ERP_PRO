package com.erp.modules.hr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 工资单查询DTO
 */
@Data
public class SalarySheetQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 工资单号 */
    private String salaryNo;

    /** 员工姓名 */
    private String employeeName;

    /** 工资月份 */
    private String salaryMonth;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
