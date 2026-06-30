package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工单实体
 * 对应表：t_work_order
 */
@Data
@TableName("t_work_order")
public class WorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工单号 */
    private String woNo;

    /** 产品ID */
    private Long productId;

    /** BOM ID */
    private Long bomId;

    /** 工单数量 */
    private BigDecimal quantity;

    /** 已完成数量 */
    private BigDecimal completedQty;

    /** 计划开始日期 */
    private LocalDate startDate;

    /** 计划结束日期 */
    private LocalDate endDate;

    /** 生产车间 */
    private String workshop;

    /** 优先级（0-普通，1-紧急，2-非常紧急） */
    private Integer priority;

    /** 单据状态（0-草稿，1-已下达，2-执行中，3-已完成，4-已关闭） */
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

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
