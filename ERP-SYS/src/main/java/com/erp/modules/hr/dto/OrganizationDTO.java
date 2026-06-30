package com.erp.modules.hr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组织架构新增/编辑DTO
 */
@Data
public class OrganizationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（更新时由Controller从路径参数设置） */
    private Long id;

    /** 父组织ID */
    private Long parentId;

    /** 组织名称 */
    @NotBlank(message = "组织名称不能为空")
    private String orgName;

    /** 组织编码 */
    private String orgCode;

    /** 组织类型（0-公司 1-部门 2-小组） */
    @NotNull(message = "组织类型不能为空")
    private Integer orgType;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0-禁用 1-启用） */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
