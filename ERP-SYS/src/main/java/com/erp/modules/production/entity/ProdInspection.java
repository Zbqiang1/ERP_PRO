package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生产检验实体
 * 对应表：t_prod_inspection
 */
@Data
@TableName("t_prod_inspection")
public class ProdInspection implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 检验单号 */
    private String inspectionNo;

    /** 生产工单ID */
    private Long woId;

    /** 检验员ID */
    private String inspectorId;

    /** 检验日期 */
    private LocalDate inspectionDate;

    /** 检验类型（0-首检，1-巡检，2-终检） */
    private Integer inspectionType;

    /** 检验结果（0-合格，1-不合格，2-让步接收） */
    private Integer inspectionResult;

    /** 合格数量 */
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    private BigDecimal defectQty;

    /** 单据状态（0-待检验，1-检验中，2-已完成） */
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
