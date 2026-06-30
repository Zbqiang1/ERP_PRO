package com.erp.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 * 对应表：t_operation_log
 *
 * @author ERP
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 操作用户ID */
    private String userId;

    /** 操作用户名 */
    private String username;

    /** 操作模块 */
    private String module;

    /** 操作描述 */
    private String operation;

    /** 请求方法（Java方法名） */
    private String method;

    /** 请求URL */
    private String requestUrl;

    /** 请求方式（GET/POST/PUT/DELETE） */
    private String requestMethod;

    /** 请求参数（TEXT类型） */
    @TableField("request_params")
    private String requestParams;

    /** 响应结果（TEXT类型） */
    @TableField("response_result")
    private String responseResult;

    /** 客户端IP */
    private String ip;

    /** 执行耗时（毫秒） */
    private Long duration;

    /** 创建时间（日志只写不更新） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
