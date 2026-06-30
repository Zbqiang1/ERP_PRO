package com.erp.modules.sales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.sales.entity.SalesOrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单明细 Mapper 接口
 */
@Mapper
public interface SalesOrderDetailMapper extends BaseMapper<SalesOrderDetail> {
}
