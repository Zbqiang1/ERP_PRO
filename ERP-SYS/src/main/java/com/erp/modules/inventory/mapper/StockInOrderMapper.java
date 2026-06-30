package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.StockInOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface StockInOrderMapper extends BaseMapper<StockInOrder> {
}
