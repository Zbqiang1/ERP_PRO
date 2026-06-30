package com.erp.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.purchase.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单 Mapper 接口
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
}
