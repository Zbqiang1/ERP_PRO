package com.erp.modules.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会计科目实体
 * 对应表：t_account_subject
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_account_subject")
public class AccountSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 科目编码 */
    private String subjectCode;

    /** 科目名称 */
    private String subjectName;

    /** 上级科目ID（0为一级科目） */
    private Long parentId;

    /** 科目类型（0-资产，1-负债，2-权益，3-成本，4-损益） */
    private Integer subjectType;

    /** 余额方向（0-借，1-贷） */
    private Integer balanceDirection;

    /** 状态（0-禁用，1-启用） */
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
