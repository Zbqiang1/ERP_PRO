package com.erp.modules.sales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.sales.entity.SalesOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单 Mapper 接口
 */
@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {
}
