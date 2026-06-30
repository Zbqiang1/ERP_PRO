package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.StockAlert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存预警 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface StockAlertMapper extends BaseMapper<StockAlert> {
}
