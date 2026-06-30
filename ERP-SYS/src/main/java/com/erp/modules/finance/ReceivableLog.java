package com.erp.modules.finance;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 应收账款流水日志实体
 * 对应表：t_receivable_log
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_receivable_log")
public class ReceivableLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 应收账款ID */
    private Long receivableId;

    /** 变动类型（0-创建，1-收款，2-核销，3-坏账） */
    private Integer changeType;

    /** 变动前金额 */
    private BigDecimal beforeAmount;

    /** 变动金额 */
    private BigDecimal changeAmount;

    /** 变动后金额 */
    private BigDecimal afterAmount;

    /** 付款方式 */
    private String paymentMethod;

    /** 参考号 */
    private String referenceNo;

    /** 操作人ID */
    private String operatorId;

    /** 创建时间（日志只写不更新） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
