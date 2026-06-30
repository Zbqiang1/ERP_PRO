package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.InventoryCheck;
import org.apache.ibatis.annotations.Mapper;

/**
 * 盘点单 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface InventoryCheckMapper extends BaseMapper<InventoryCheck> {
}
