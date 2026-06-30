package com.erp.modules.hr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 组织架构查询DTO
 */
@Data
public class OrganizationQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 组织名称 */
    private String orgName;

    /** 组织编码 */
    private String orgCode;

    /** 组织类型 */
    private Integer orgType;

    /** 当前页码 */
    private Long page;

    /** 每页大小 */
    private Long size;
}
