package com.erp.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.system.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门Mapper接口
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
