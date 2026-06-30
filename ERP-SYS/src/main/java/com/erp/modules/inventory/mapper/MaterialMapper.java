package com.erp.modules.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.inventory.Material;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物料 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface MaterialMapper extends BaseMapper<Material> {
}
