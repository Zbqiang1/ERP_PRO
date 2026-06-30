package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * BOM明细实体（明细表，不冗余物料编码/名称）
 * 对应表：t_bom_detail
 */
@Data
@TableName("t_bom_detail")
public class BomDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** BOM主表ID */
    private Long bomId;

    /** 父物料ID */
    private Long parentMaterialId;

    /** 子物料ID */
    private Long materialId;

    /** 用量 */
    private BigDecimal quantity;

    /** 单位 */
    private String unit;

    /** 损耗率 */
    private BigDecimal scrapRate;

    /** 排序 */
    private Integer sortOrder;
}
