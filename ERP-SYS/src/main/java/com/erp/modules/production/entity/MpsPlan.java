package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 主生产计划实体
 * 对应表：t_mps_plan
 */
@Data
@TableName("t_mps_plan")
public class MpsPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 计划单号 */
    private String planNo;

    /** 计划月份（YYYY-MM） */
    private String planMonth;

    /** 产品ID */
    private Long productId;

    /** 计划数量 */
    private BigDecimal plannedQty;

    /** 已完成数量 */
    private BigDecimal completedQty;

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
