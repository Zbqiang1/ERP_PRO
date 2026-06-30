package com.erp.modules.hr.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效考核视图对象
 */
@Data
public class PerformanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long employeeId;

    /** 员工姓名 */
    private String employeeName;

    /** 考核周期 */
    private String assessmentPeriod;

    /** 考核人 */
    private String assessor;

    /** KPI分数 */
    private BigDecimal kpiScore;

    /** 考核评语 */
    private String evaluation;

    /** 等级（A/B/C/D） */
    private String grade;

    /** 状态 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
