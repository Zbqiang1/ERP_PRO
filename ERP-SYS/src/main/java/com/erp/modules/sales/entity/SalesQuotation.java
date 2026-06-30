package com.erp.modules.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 销售报价单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sales_quotation")
public class SalesQuotation implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 报价单号 */
    private String quotationNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称（历史快照） */
    private String customerName;

    /** 报价日期 */
    private LocalDate quotationDate;

    /** 有效期至 */
    private LocalDate validUntil;

    /** 报价总金额 */
    private BigDecimal totalAmount;

    /** 单据状态（0-草稿，1-已发送，2-已确认，3-已关闭） */
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
