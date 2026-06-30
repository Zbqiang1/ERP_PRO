package com.erp.modules.production.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.production.entity.WorkOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单 Mapper接口
 */
@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
}
