package com.erp.modules.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考勤实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_attendance")
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 员工ID */
    private Long employeeId;

    /** 考勤日期 */
    private LocalDate attendanceDate;

    /** 签到时间 */
    private LocalDateTime checkInTime;

    /** 签退时间 */
    private LocalDateTime checkOutTime;

    /** 考勤类型（0-正常，1-迟到，2-早退，3-旷工，4-请假，5-出差，6-加班） */
    private Integer attendanceType;

    /** 单据状态（0-已记录，1-已确认） */
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
