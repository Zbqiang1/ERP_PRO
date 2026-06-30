package com.erp.modules.purchase.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 供应商视图对象
 */
@Data
public class SupplierVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 供应商编码 */
    private String supplierCode;

    /** 供应商名称 */
    private String supplierName;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 地址 */
    private String address;

    /** 开户银行 */
    private String bankName;

    /** 银行账号 */
    private String bankAccount;

    /** 税号 */
    private String taxNo;

    /** 状态（0-停用 1-启用） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
