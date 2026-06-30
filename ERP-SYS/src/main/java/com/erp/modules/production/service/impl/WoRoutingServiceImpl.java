package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.WoRouting;
import com.erp.modules.production.mapper.WoRoutingMapper;
import com.erp.modules.production.service.IWoRoutingService;
import org.springframework.stereotype.Service;

/**
 * 工单工艺路线服务实现
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Service
public class WoRoutingServiceImpl extends ServiceImpl<WoRoutingMapper, WoRouting> implements IWoRoutingService {
}
