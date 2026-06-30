package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存预警实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_stock_alert")
public class StockAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 预警单号 */
    private String alertNo;

    /** 物料ID */
    private Long materialId;

    /** 仓库ID */
    private Long warehouseId;

    /** 当前库存 */
    private BigDecimal currentStock;

    /** 安全库存 */
    private BigDecimal safetyStock;

    /** 预警类型（0-低于安全库存，1-高于最大库存，2-呆滞物料） */
    private Integer alertType;

    /** 预警日期 */
    private LocalDate alertDate;

    /** 单据状态（0-未处理，1-已处理，2-已忽略） */
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
