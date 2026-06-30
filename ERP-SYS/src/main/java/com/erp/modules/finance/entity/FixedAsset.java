package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 固定资产实体
 * 对应表：t_fixed_asset
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_fixed_asset")
public class FixedAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 资产编号 */
    private String assetNo;

    /** 资产名称 */
    private String assetName;

    /** 资产类别 */
    private String category;

    /** 使用部门ID */
    private String deptId;

    /** 原值 */
    private BigDecimal originalValue;

    /** 净值 */
    private BigDecimal currentValue;

    /** 折旧率 */
    private BigDecimal depreciationRate;

    /** 购买日期 */
    private LocalDate purchaseDate;

    /** 单据状态（0-使用中，1-闲置，2-报废，3-处置） */
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

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除（0-未删除，1-已删除） */
    @TableLogic
    private Integer deleted;
}
