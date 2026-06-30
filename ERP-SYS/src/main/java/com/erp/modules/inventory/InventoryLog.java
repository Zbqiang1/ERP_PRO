package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存流水日志实体
 * 对应表：t_inventory_log
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_inventory_log")
public class InventoryLog implements Serializable {

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

    /** 变动类型（0-入库，1-出库，2-调拨，3-盘点调整） */
    private Integer changeType;

    /** 变动前数量 */
    private BigDecimal beforeQty;

    /** 变动数量 */
    private BigDecimal changeQty;

    /** 变动后数量 */
    private BigDecimal afterQty;

    /** 关联单据类型 */
    private String relatedOrderType;

    /** 关联单据ID */
    private Long relatedOrderId;

    /** 关联单据号 */
    private String relatedOrderNo;

    /** 批次号 */
    private String batchNo;

    /** 操作人ID */
    private String operatorId;

    /** 创建时间（日志只写不更新） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
