package com.erp.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.purchase.entity.PurchaseOrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单明细 Mapper 接口
 */
@Mapper
public interface PurchaseOrderDetailMapper extends BaseMapper<PurchaseOrderDetail> {
}
