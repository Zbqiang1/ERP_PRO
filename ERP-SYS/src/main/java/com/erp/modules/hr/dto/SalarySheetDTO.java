package com.erp.modules.hr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工资单新增/编辑DTO
 */
@Data
public class SalarySheetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 工资单号 */
    private String salaryNo;

    /** 员工ID */
    @NotNull(message = "员工不能为空")
    private Long employeeId;

    /** 员工姓名 */
    private String employeeName;

    /** 工资月份 */
    @NotBlank(message = "工资月份不能为空")
    private String salaryMonth;

    /** 基本工资 */
    @NotNull(message = "基本工资不能为空")
    private BigDecimal basicSalary;

    /** 绩效奖金 */
    private BigDecimal performanceBonus;

    /** 加班工资 */
    private BigDecimal overtimePay;

    /** 扣款 */
    private BigDecimal deduction;

    /** 社保 */
    private BigDecimal socialInsurance;

    /** 个税 */
    private BigDecimal tax;

    /** 状态 */
    private Integer status;
}
