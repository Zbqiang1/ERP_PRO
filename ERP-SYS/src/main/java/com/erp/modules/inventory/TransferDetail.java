package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 调拨单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_transfer_detail")
public class TransferDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 调拨单主表ID */
    private Long transferId;

    /** 物料ID */
    private Long materialId;

    /** 调出库位ID */
    private Long fromLocationId;

    /** 调入库位ID */
    private Long toLocationId;

    /** 调拨数量 */
    private BigDecimal quantity;

    /** 批次号 */
    private String batchNo;
}
