package com.erp.modules.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.purchase.entity.PurchaseRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购申请 Mapper 接口
 */
@Mapper
public interface PurchaseRequestMapper extends BaseMapper<PurchaseRequest> {
}
