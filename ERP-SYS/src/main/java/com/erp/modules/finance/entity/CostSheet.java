package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成本核算单实体
 * 对应表：t_cost_sheet
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_cost_sheet")
public class CostSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 成本核算单号 */
    private String costNo;

    /** 产品ID */
    private Long productId;

    /** 产品名称（历史快照） */
    private String productName;

    /** 材料成本 */
    private BigDecimal materialCost;

    /** 人工成本 */
    private BigDecimal laborCost;

    /** 制造费用 */
    private BigDecimal overheadCost;

    /** 总成本 */
    private BigDecimal totalCost;

    /** 成本期间（YYYY-MM） */
    private String costPeriod;

    /** 单据状态（0-草稿，1-已确认，2-已审核） */
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

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除（0-未删除，1-已删除） */
    @TableLogic
    private Integer deleted;
}
