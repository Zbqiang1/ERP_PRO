package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工单工艺路线明细实体
 * 对应表：t_wo_routing
 */
@Data
@TableName("t_wo_routing")
public class WoRouting implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工单主表ID */
    private Long woId;

    /** 工序序号 */
    private Integer operationSeq;

    /** 工序名称 */
    private String operationName;

    /** 工作中心 */
    private String workcenter;

    /** 计划工时 */
    private BigDecimal plannedHours;

    /** 实际工时 */
    private BigDecimal actualHours;

    /** 操作工ID */
    private String workerId;

    /** 状态（0-待加工，1-加工中，2-已完成） */
    private Integer routingStatus;
}
