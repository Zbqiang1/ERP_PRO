package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 凭证分录明细实体
 * 对应表：t_voucher_entry
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_voucher_entry")
public class VoucherEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 凭证主表ID */
    private Long voucherId;

    /** 会计科目ID */
    private Long subjectId;

    /** 摘要 */
    private String summary;

    /** 借方金额 */
    private BigDecimal debitAmount;

    /** 贷方金额 */
    private BigDecimal creditAmount;

    /** 排序 */
    private Integer sortOrder;
}
