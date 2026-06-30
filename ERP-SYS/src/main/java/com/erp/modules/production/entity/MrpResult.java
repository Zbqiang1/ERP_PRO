package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * MRP运算结果明细实体（明细表，不冗余物料编码/名称等）
 * 对应表：t_mrp_result
 */
@Data
@TableName("t_mrp_result")
public class MrpResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 计划主表ID */
    private Long planId;

    /** 物料ID */
    private Long materialId;

    /** 毛需求 */
    private BigDecimal grossRequirement;

    /** 现有库存量 */
    private BigDecimal onHandQty;

    /** 在途量 */
    private BigDecimal onOrderQty;

    /** 净需求 */
    private BigDecimal netRequirement;

    /** 计划订单下达 */
    private String plannedOrderRelease;

    /** 计划订单接收 */
    private String plannedOrderReceipt;
}
