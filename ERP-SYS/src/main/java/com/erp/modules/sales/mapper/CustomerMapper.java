package com.erp.modules.sales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.sales.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户 Mapper 接口
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
