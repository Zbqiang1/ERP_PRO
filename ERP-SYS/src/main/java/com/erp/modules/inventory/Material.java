package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 物料类别ID */
    private Long categoryId;

    /** 物料类别名称 */
    private String categoryName;

    /** 规格型号 */
    private String spec;

    /** 单位 */
    private String unit;

    /** 安全库存 */
    private BigDecimal safetyStock;

    /** 最大库存 */
    private BigDecimal maxStock;

    /** 最小库存 */
    private BigDecimal minStock;

    /** 参考单价 */
    private BigDecimal unitPrice;

    /** 状态（0-禁用，1-启用） */
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
