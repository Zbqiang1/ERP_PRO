package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.Location;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库位 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface LocationMapper extends BaseMapper<Location> {
}
