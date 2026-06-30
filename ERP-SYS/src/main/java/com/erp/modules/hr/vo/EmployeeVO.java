package com.erp.modules.hr.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工视图对象
 */
@Data
public class EmployeeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工编号 */
    private String employeeNo;

    /** 真实姓名 */
    private String realName;

    /** 性别（0-男 1-女） */
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
    private Long orgId;

    /** 组织名称 */
    private String orgName;

    /** 职位 */
    private String position;

    /** 入职日期 */
    private LocalDate hireDate;

    /** 离职日期 */
    private LocalDate leaveDate;

    /** 状态（0-在职 1-离职 2-实习） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
