package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.StockOutOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface StockOutOrderMapper extends BaseMapper<StockOutOrder> {
}
