package com.erp.modules.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工资单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_salary_sheet")
public class SalarySheet implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 工资单号 */
    private String salaryNo;

    /** 员工ID */
    private Long employeeId;

    /** 工资月份（YYYY-MM） */
    private String salaryMonth;

    /** 基本工资 */
    private BigDecimal basicSalary;

    /** 绩效奖金 */
    private BigDecimal performanceBonus;

    /** 加班费 */
    private BigDecimal overtimePay;

    /** 扣款 */
    private BigDecimal deduction;

    /** 社保扣款 */
    private BigDecimal socialInsurance;

    /** 个税 */
    private BigDecimal tax;

    /** 实发工资 */
    private BigDecimal netSalary;

    /** 单据状态（0-草稿，1-已确认，2-已发放） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 创建人用户ID */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 修改人用户ID */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除（0-未删除，1-已删除） */
    @TableLogic
    private Integer deleted;
}
