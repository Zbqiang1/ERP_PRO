package com.erp.modules.inventory;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存预警日志实体
 * 对应表：t_stock_alert_log
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_stock_alert_log")
public class StockAlertLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 预警ID */
    private Long alertId;

    /** 物料ID */
    private Long materialId;

    /** 仓库ID */
    private Long warehouseId;

    /** 预警类型 */
    private Integer alertType;

    /** 预警级别 */
    private Integer alertLevel;

    /** 预警信息 */
    private String alertMessage;

    /** 处理人ID */
    private String handledBy;

    /** 处理时间 */
    private LocalDateTime handledTime;

    /** 处理结果 */
    private String handleResult;

    /** 创建时间（日志只写不更新） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
