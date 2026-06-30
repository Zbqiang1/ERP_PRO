package com.erp.modules.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 工号 */
    private String employeeNo;

    /** 姓名 */
    private String realName;

    /** 性别（0-女，1-男） */
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthday;

    /** 身份证号 */
    private String idCard;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 所属组织ID */
    private Long orgId;

    /** 职位 */
    private String position;

    /** 入职日期 */
    private LocalDate hireDate;

    /** 离职日期 */
    private LocalDate leaveDate;

    /** 状态（0-离职，1-在职，2-实习） */
    private Integer status;

    /** 备注 */
    private String remark;

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
