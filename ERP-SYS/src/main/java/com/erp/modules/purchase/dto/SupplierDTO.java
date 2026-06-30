package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 供应商新增/编辑DTO
 */
@Data
public class SupplierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供应商名称 */
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;

    /** 供应商编码 */
    private String supplierCode;

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
    @NotNull(message = "状态不能为空")
    private Integer status;
}
