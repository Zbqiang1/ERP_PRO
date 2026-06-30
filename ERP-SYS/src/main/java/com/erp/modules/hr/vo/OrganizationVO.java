package com.erp.modules.hr.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织架构视图对象
 */
@Data
public class OrganizationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 父组织ID */
    private Long parentId;

    /** 组织名称 */
    private String orgName;

    /** 组织编码 */
    private String orgCode;

    /** 组织类型（0-公司 1-部门 2-小组） */
    private Integer orgType;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0-禁用 1-启用） */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
