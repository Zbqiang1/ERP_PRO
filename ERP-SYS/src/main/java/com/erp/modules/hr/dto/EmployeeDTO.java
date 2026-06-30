package com.erp.modules.hr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 员工新增/编辑DTO
 */
@Data
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工编号 */
    private String employeeNo;

    /** 真实姓名 */
    @NotBlank(message = "姓名不能为空")
    private String realName;

    /** 性别（0-男 1-女） */
    @NotNull(message = "性别不能为空")
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthday;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 组织ID */
    @NotNull(message = "所属组织不能为空")
    private Long orgId;

    /** 组织名称 */
    private String orgName;

    /** 职位 */
    private String position;

    /** 入职日期 */
    private LocalDate hireDate;

    /** 状态（0-在职 1-离职 2-实习） */
    @NotNull(message = "员工状态不能为空")
    private Integer status;
}
