package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 盘点单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_inventory_check")
public class InventoryCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 盘点单号 */
    private String checkNo;

    /** 仓库ID */
    private Long warehouseId;

    /** 盘点类型（0-周期盘点，1-随机盘点，2-全面盘点） */
    private Integer checkType;

    /** 盘点日期 */
    private LocalDate checkDate;

    /** 盘点状态（0-盘点中，1-已确认，2-已调整） */
    private Integer checkStatus;

    /** 单据状态 */
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
