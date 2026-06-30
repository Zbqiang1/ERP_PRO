package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入库单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_stock_in_order")
public class StockInOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 入库单号 */
    private String inNo;

    /** 入库类型（0-采购入库，1-生产入库，2-调拨入库，3-退货入库，4-盘点盘盈，5-其他入库） */
    private Integer inType;

    /** 仓库ID */
    private Long warehouseId;

    /** 关联单据ID */
    private Long relatedOrderId;

    /** 关联单据号 */
    private String relatedOrderNo;

    /** 入库总金额 */
    private BigDecimal totalAmount;

    /** 入库日期 */
    private LocalDate inDate;

    /** 单据状态（0-草稿，1-已入库，2-已记账） */
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
