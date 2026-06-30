package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户视图对象
 */
@Data
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户编码 */
    private String customerCode;

    /** 客户名称 */
    private String customerName;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 地址 */
    private String address;

    /** 信用额度 */
    private BigDecimal creditLimit;

    /** 状态（0-停用 1-启用） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
