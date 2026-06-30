package com.erp.modules.hr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 绩效考核查询DTO
 */
@Data
public class PerformanceQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工姓名 */
    private String employeeName;

    /** 考核周期 */
    private String assessmentPeriod;

    /** 等级 */
    private String grade;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
