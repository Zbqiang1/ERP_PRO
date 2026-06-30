package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 税务发票实体
 * 对应表：t_tax_invoice
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_tax_invoice")
public class TaxInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 发票号 */
    private String invoiceNo;

    /** 发票类型（0-增值税专用发票，1-增值税普通发票） */
    private Integer invoiceType;

    /** 开票日期 */
    private LocalDate invoiceDate;

    /** 购方名称 */
    private String buyerName;

    /** 销方名称 */
    private String sellerName;

    /** 金额（不含税） */
    private BigDecimal amount;

    /** 税率（%） */
    private BigDecimal taxRate;

    /** 税额 */
    private BigDecimal taxAmount;

    /** 价税合计 */
    private BigDecimal totalAmount;

    /** 单据状态（0-未认证，1-已认证，2-已抵扣） */
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
