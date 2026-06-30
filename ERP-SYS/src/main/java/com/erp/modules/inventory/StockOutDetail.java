package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出库单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_stock_out_detail")
public class StockOutDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 出库单主表ID */
    private Long outId;

    /** 物料ID */
    private Long materialId;

    /** 库位ID */
    private Long locationId;

    /** 出库数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 金额 */
    private BigDecimal amount;

    /** 批次号 */
    private String batchNo;

    /** 排序 */
    private Integer sortOrder;
}
