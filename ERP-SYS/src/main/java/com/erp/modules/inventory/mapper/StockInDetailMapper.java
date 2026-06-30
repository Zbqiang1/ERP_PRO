package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.StockInDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库明细 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface StockInDetailMapper extends BaseMapper<StockInDetail> {
}
