package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.StockOutDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库明细 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface StockOutDetailMapper extends BaseMapper<StockOutDetail> {
}
