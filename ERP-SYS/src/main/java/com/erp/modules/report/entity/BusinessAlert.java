package com.erp.modules.report.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 业务预警实体
 * 对应表：t_business_alert
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_business_alert")
public class BusinessAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 预警名称 */
    private String alertName;

    /** 预警类型（0-库存预警，1-应收账款预警，2-质量异常预警，3-交期预警） */
    private Integer alertType;

    /** 预警规则 */
    private String alertRule;

    /** 预警级别（0-低，1-中，2-高） */
    private Integer alertLevel;

    /** 状态（0-禁用，1-启用） */
    private Integer status;

    /** 上次触发时间 */
    private LocalDateTime lastTriggerTime;

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
