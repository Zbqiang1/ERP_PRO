package com.erp.modules.production.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.production.entity.ProdInspection;
import org.apache.ibatis.annotations.Mapper;

/**
 * 生产检验单数据访问层
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Mapper
public interface ProdInspectionMapper extends BaseMapper<ProdInspection> {
}
