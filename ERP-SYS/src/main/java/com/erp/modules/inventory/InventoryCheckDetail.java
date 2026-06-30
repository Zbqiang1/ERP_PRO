package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_inventory_check_detail")
public class InventoryCheckDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 盘点单主表ID */
    private Long checkId;

    /** 物料ID */
    private Long materialId;

    /** 库位ID */
    private Long locationId;

    /** 账面数量 */
    private BigDecimal bookQty;

    /** 实盘数量 */
    private BigDecimal actualQty;

    /** 差异数量 */
    private BigDecimal diffQty;

    /** 差异原因 */
    private String diffReason;
}
