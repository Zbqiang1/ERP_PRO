package com.erp.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.report.entity.DashboardWidget;
import com.erp.modules.report.mapper.DashboardWidgetMapper;
import com.erp.modules.report.service.IDashboardWidgetService;
import org.springframework.stereotype.Service;

/**
 * 仪表盘组件 Service实现类
 */
@Service
public class DashboardWidgetServiceImpl extends ServiceImpl<DashboardWidgetMapper, DashboardWidget> implements IDashboardWidgetService {
}
