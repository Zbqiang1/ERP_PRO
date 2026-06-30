package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存余额实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 仓库ID */
    private Long warehouseId;

    /** 库位ID */
    private Long locationId;

    /** 物料ID */
    private Long materialId;

    /** 现有库存量 */
    private BigDecimal onHandQty;

    /** 锁定库存量 */
    private BigDecimal lockedQty;

    /** 可用库存量 */
    private BigDecimal availableQty;

    /** 最后盘点时间 */
    private LocalDateTime lastCountTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
