package com.erp.modules.hr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 绩效考核新增/编辑DTO
 */
@Data
public class PerformanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（更新时由Controller从路径参数设置） */
    private Long id;

    /** 员工ID */
    @NotNull(message = "员工不能为空")
    private Long employeeId;

    /** 员工姓名 */
    private String employeeName;

    /** 考核周期 */
    @NotBlank(message = "考核周期不能为空")
    private String assessmentPeriod;

    /** 考核人 */
    private String assessor;

    /** KPI分数 */
    @NotNull(message = "KPI分数不能为空")
    private BigDecimal kpiScore;

    /** 考核评语 */
    private String evaluation;

    /** 等级（A/B/C/D） */
    @NotBlank(message = "考核等级不能为空")
    private String grade;

    /** 状态 */
    private Integer status;
}
