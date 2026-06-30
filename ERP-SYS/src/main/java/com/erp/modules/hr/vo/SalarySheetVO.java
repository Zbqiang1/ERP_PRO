package com.erp.modules.hr.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工资单视图对象
 */
@Data
public class SalarySheetVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 工资单号 */
    private String salaryNo;

    /** 员工ID */
    private Long employeeId;

    /** 员工姓名 */
    private String employeeName;

    /** 工资月份 */
    private String salaryMonth;

    /** 基本工资 */
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

    /** 实发工资 */
    private BigDecimal netSalary;

    /** 状态 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
