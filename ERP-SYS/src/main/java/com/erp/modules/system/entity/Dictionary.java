package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据字典实体
 * 对应表：t_dictionary
 */
@Data
@TableName("t_dictionary")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 字典类型 */
    private String dictType;

    /** 字典标签 */
    private String dictLabel;

    /** 字典值 */
    private String dictValue;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0-禁用 1-启用） */
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

    /** 逻辑删除标识（0-未删除 1-已删除） */
    @TableLogic
    private Integer deleted;
}
