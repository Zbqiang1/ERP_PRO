package com.erp.modules.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 绩效考核实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_performance")
public class Performance implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 员工ID */
    private Long employeeId;

    /** 考核周期（YYYY-MM） */
    private String assessmentPeriod;

    /** 考核人ID */
    private String assessorId;

    /** KPI得分 */
    private BigDecimal kpiScore;

    /** 考核评价 */
    private String evaluation;

    /** 考核等级（A/B/C/D） */
    private String grade;

    /** 单据状态（0-草稿，1-已确认，2-已归档） */
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
