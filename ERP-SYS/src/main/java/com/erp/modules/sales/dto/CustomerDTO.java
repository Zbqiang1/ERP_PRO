package com.erp.modules.sales.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户新增/编辑DTO
 */
@Data
public class CustomerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 客户名称 */
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    /** 客户编码 */
    private String customerCode;

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
    @NotNull(message = "状态不能为空")
    private Integer status;
}
