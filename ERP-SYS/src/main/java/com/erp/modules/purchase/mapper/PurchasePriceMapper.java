package com.erp.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.purchase.entity.PurchasePrice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购价格 Mapper 接口
 */
@Mapper
public interface PurchasePriceMapper extends BaseMapper<PurchasePrice> {
}
