package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 调拨单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_transfer_order")
public class TransferOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 调拨单号 */
    private String transferNo;

    /** 调出仓库ID */
    private Long fromWarehouseId;

    /** 调入仓库ID */
    private Long toWarehouseId;

    /** 调拨总金额 */
    private BigDecimal totalAmount;

    /** 调拨日期 */
    private LocalDate transferDate;

    /** 单据状态（0-草稿，1-已调拨，2-已确认） */
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
