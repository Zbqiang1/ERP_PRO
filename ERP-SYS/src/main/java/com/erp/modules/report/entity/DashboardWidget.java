package com.erp.modules.report.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 仪表盘组件实体
 * 对应表：t_dashboard_widget
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_dashboard_widget")
public class DashboardWidget implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 组件名称 */
    private String widgetName;

    /** 组件类型（0-数字卡片，1-柱状图，2-折线图，3-饼图） */
    private Integer widgetType;

    /** SQL查询语句 */
    private String sqlQuery;

    /** 刷新间隔（秒） */
    private Integer refreshInterval;

    /** 排序号 */
    private Integer sortOrder;

    /** 状态（0-禁用，1-启用） */
    private Integer status;

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
