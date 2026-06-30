package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 委外加工单实体
 * 对应表：t_subcontract
 */
@Data
@TableName("t_subcontract")
public class Subcontract implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 委外合同号 */
    private String contractNo;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称（历史快照） */
    private String supplierName;

    /** 物料ID */
    private Long materialId;

    /** 数量 */
    private BigDecimal quantity;

    /** 合同单价 */
    private BigDecimal contractPrice;

    /** 发出日期 */
    private LocalDate sendDate;

    /** 预计返回日期 */
    private LocalDate expectedReturnDate;

    /** 实际返回日期 */
    private LocalDate actualReturnDate;

    /** 单据状态（0-草稿，1-已发出，2-部分返回，3-已返回，4-已结算） */
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

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
