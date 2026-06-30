package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 记账凭证实体
 * 对应表：t_voucher
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_voucher")
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 凭证号 */
    private String voucherNo;

    /** 凭证日期 */
    private LocalDate voucherDate;

    /** 凭证类型（0-记账凭证，1-转账凭证，2-调整凭证） */
    private Integer voucherType;

    /** 借方合计金额 */
    private BigDecimal totalDebit;

    /** 贷方合计金额 */
    private BigDecimal totalCredit;

    /** 审核人ID */
    private String auditorId;

    /** 审核时间 */
    private LocalDateTime auditTime;

    /** 单据状态（0-草稿，1-已审核，2-已过账） */
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
