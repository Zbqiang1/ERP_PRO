package com.erp.modules.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.hr.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织架构 Mapper 接口
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {
}
