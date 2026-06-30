package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.TransferOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调拨单 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface TransferOrderMapper extends BaseMapper<TransferOrder> {
}
