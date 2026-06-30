package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.WorkOrder;
import com.erp.modules.production.mapper.WorkOrderMapper;
import com.erp.modules.production.service.IWorkOrderService;
import org.springframework.stereotype.Service;

/**
 * 工单 Service实现类
 */
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements IWorkOrderService {
}
