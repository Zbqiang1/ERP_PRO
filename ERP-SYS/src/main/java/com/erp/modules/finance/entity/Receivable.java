package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收账款实体
 * 对应表：t_receivable
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_receivable")
public class Receivable implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 应收单号 */
    private String receivableNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称（历史快照） */
    private String customerName;

    /** 应收金额 */
    private BigDecimal amount;

    /** 已收金额 */
    private BigDecimal receivedAmount;

    /** 余额 */
    private BigDecimal balance;

    /** 到期日期 */
    private LocalDate dueDate;

    /** 单据状态（0-未收款，1-部分收款，2-已收款，3-已核销，4-坏账） */
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
