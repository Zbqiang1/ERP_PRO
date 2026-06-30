package com.erp.modules.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * BOM表头实体
 * 对应表：t_bom_header
 */
@Data
@TableName("t_bom_header")
public class BomHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** BOM编号 */
    private String bomNo;

    /** 产品ID（成品物料ID） */
    private Long productId;

    /** 产品名称 */
    private String productName;

    /** 版本号 */
    private String version;

    /** 生效日期 */
    private LocalDate effectiveDate;

    /** 失效日期 */
    private LocalDate expireDate;

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

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
