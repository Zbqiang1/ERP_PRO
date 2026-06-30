package com.erp.modules.hr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工查询DTO
 */
@Data
public class EmployeeQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工编号 */
    private String employeeNo;

    /** 真实姓名 */
    private String realName;

    /** 联系电话 */
    private String phone;

    /** 组织ID */
    private Long orgId;

    /** 状态（0-在职 1-离职 2-实习） */
    private String status;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
